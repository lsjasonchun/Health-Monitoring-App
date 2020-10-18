package com.example.health_monitoring_app;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.UUID;

public class BluetoothConnectionService {
    private static final String TAG = "BluetoothConnService";

    private static final String appName = "Health Monitoring Device";

    private static final UUID MY_UUID_INSECURE =
            UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    private final BluetoothAdapter mBluetoothAdapter;
    Context mContext;

    private AcceptThread mAcceptThread;
    private ConnectThread mConnectThread;
    private BluetoothDevice mmDevice;
    private UUID deviceUUID;
    ProgressDialog mProgressDialog;

    private ConnectedThread mConnectedThread;

    public BluetoothConnectionService(Context context) {
        mContext = context;
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    }

    private class AcceptThread extends Thread {
        private final BluetoothServerSocket mmServerSocket;

        public AcceptThread() {
            BluetoothServerSocket tmp = null;

            try {
                tmp = mBluetoothAdapter.listenUsingRfcommWithServiceRecord(appName, MY_UUID_INSECURE);
                Log.d(TAG, "AcceptThread: Setting up Server using: " + MY_UUID_INSECURE);
            } catch (IOException e) {

            }
            mmServerSocket = tmp;


        }

        @Override
        public void run() {
            Log.d(TAG, "run: AcceptThread Running " + MY_UUID_INSECURE);

            BluetoothSocket socket = null;

            try {
                socket = mmServerSocket.accept();
            } catch (IOException e) {
                Log.d(TAG, "run: AcceptThread: IOEXCEPTION: " + e.getMessage());
            }

            if (socket != null) {
                connected(socket, mmDevice);
            }
            Log.d(TAG, "END of AcceptThread");
        }

        public void cancel() {
            Log.d(TAG, "cancel: Cancelling AcceptThread");
            try {
                mmServerSocket.close();
            } catch (IOException e) {
                Log.d(TAG, "cancel: Close of AcceptThread ServerSocket failed: IOEXCEPTION: " + e.getMessage());
            }
        }
    }

    private class ConnectThread extends Thread {
        private BluetoothSocket mmSocket;

        public ConnectThread(BluetoothDevice device, UUID uuid) {
            Log.d(TAG, "ConnectThread: started");
            mmDevice = device;
            deviceUUID = uuid;
        }

        @Override
        public void run() {
            BluetoothSocket tmp = null;
            Log.d(TAG, "run: ConnectThread");

            try {
                Log.d(TAG, "ConnectThread: Trying to create InsecureRfcommSocket using UUID");
                tmp = mmDevice.createRfcommSocketToServiceRecord(deviceUUID);
            } catch (IOException e) {
                Log.d(TAG, "ConnectThread: Could not create InsecureRfcommSocket: " + e.getMessage());
            }

            mmSocket = tmp;
            mBluetoothAdapter.cancelDiscovery();

            try {
                mmSocket.connect();
                Log.d(TAG, "run: ConnectThread connected");
            } catch (IOException e) {
                try {
                    mmSocket.close();
                    Log.d(TAG, "run: Closed Socket");
                } catch (IOException ex) {
                    Log.d(TAG, "ConnectThread: run: Unable to close connection in socket: " + ex.getMessage());
                }
                Log.d(TAG, "run: ConnectThread: Could not connect to UUID: " +  MY_UUID_INSECURE);
            }

            connected(mmSocket, mmDevice);
        }

        public void cancel() {
            try {
                Log.d(TAG, "cancel: Closing Client Socket");
                mmSocket.close();
            } catch (IOException e) {
                Log.d(TAG, "cancel: close() of mmSocket in ConnectThread failed: " + e.getMessage());
            }
        }
    }

    public synchronized void start() {
        Log.d(TAG, "start");

        if(mConnectThread != null) {
            mConnectThread.cancel();
            mConnectThread = null;
        }

        if(mAcceptThread == null) {
            mAcceptThread = new AcceptThread();
            mAcceptThread.start();
        }
    }

    public void startClient(BluetoothDevice device, UUID uuid) {
        Log.d(TAG, "startClient: Started");

        mProgressDialog = ProgressDialog.show(mContext, "Connecting Bluetooth",
                "Please wait...", true);

        mConnectThread = new ConnectThread(device, uuid);
        mConnectThread.start();
    }

    private class ConnectedThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final InputStream mmInputStream;
        private final OutputStream mmOutputStream;

        public ConnectedThread(BluetoothSocket socket) {
            Log.d(TAG, "ConnectedThread: Start");

            mmSocket = null;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            mProgressDialog.dismiss();

            try {
                tmpIn = mmSocket.getInputStream();
                tmpOut = mmSocket.getOutputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }

            mmInputStream = tmpIn;
            mmOutputStream = tmpOut;
        }

        @Override
        public void run() {
            byte[] buffer = new byte[1024];

            int bytes;

            while(true) {
                try {
                    bytes = mmInputStream.read(buffer);
                    String incomingMsgs = new String(buffer, 0, bytes);
                    Log.d(TAG, "run ConnectedThread: InputStream: " + incomingMsgs);
                } catch (IOException e) {
                    Log.d(TAG, "run ConnectedThread: Error writing to inputstream " + e.getMessage());
                    break;
                }
            }
        }

        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void write(byte[] bytes) {
            String text = new String(bytes, Charset.defaultCharset());
            Log.d(TAG, "write ConnectedThread: Writing to outputStream: " + text);

            try {
                mmOutputStream.write(bytes);
            } catch (IOException e) {
                Log.d(TAG, "write ConnectedThread: Error writing to outputstream " + e.getMessage());
            }
        }
    }

    private void connected(BluetoothSocket mmSocket, BluetoothDevice mmDevice) {
        Log.d(TAG, "connected: starting");

        mConnectedThread = new ConnectedThread(mmSocket);
        mConnectedThread.start();
    }

    public void write(byte[] out) {
        ConnectedThread r;
        Log.d(TAG, "write: Write Called");
        mConnectedThread.write(out);
    }
}
