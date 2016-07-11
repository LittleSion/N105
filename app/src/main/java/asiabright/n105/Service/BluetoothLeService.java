/*
 * Copyright Cypress Semiconductor Corporation, 2014-2014-2015 All rights reserved.
 * 
 * This software, associated documentation and materials ("Software") is
 * owned by Cypress Semiconductor Corporation ("Cypress") and is
 * protected by and subject to worldwide patent protection (UnitedStates and foreign), United States copyright laws and international
 * treaty provisions. Therefore, unless otherwise specified in a separate license agreement between you and Cypress, this Software
 * must be treated like any other copyrighted material. Reproduction,
 * modification, translation, compilation, or representation of this
 * Software in any other form (e.g., paper, magnetic, optical, silicon)
 * is prohibited without Cypress's express written permission.
 * 
 * Disclaimer: THIS SOFTWARE IS PROVIDED AS-IS, WITH NO WARRANTY OF ANY
 * KIND, EXPRESS OR IMPLIED, INCLUDING, BUT NOT LIMITED TO,
 * NONINFRINGEMENT, IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS
 * FOR A PARTICULAR PURPOSE. Cypress reserves the right to make changes
 * to the Software without notice. Cypress does not assume any liability
 * arising out of the application or use of Software or any product or
 * circuit described in the Software. Cypress does not authorize its
 * products for use as critical components in any products where a
 * malfunction or failure may reasonably be expected to result in
 * significant injury or death ("High Risk Product"). By including
 * Cypress's product in a High Risk Product, the manufacturer of such
 * system or application assumes all risk of such use and in doing so
 * indemnifies Cypress against all liability.
 * 
 * Use of this Software may be limited by and subject to the applicable
 * Cypress software license agreement.
 * 
 * 
 */

package asiabright.n105.Service;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import com.litesuits.bluetooth.LiteBluetooth;
import com.litesuits.bluetooth.scan.PeriodScanCallback;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


import asiabright.n105.Util.Logger;
import asiabright.n105.Util.Constants;
import asiabright.n105.Util.GattAttributes;
import asiabright.n105.Util.MyList;
import asiabright.n105.Util.MySwitch;
import asiabright.n105.Util.UUIDDatabase;
import asiabright.n105.Util.Utils;
import asiabright.n105.db.DBManager;

/**
 * Service for managing connection and data communication with a GATT server
 * hosted on a given BlueTooth LE device.
 */
public class BluetoothLeService extends Service {


    private static int TIME_OUT_SCAN = 10000;
    private DBManager dbManager;
    /**
     * GATT Status constants
     */
    public final static String SERVICEACTION = "bluetoothleservice";
    public final static String TAG = "BluetoothLeService";
    public final static String ACTION_GATT_CONNECTED =
            "com.example.bluetooth.le.ACTION_GATT_CONNECTED";
    public final static String ACTION_GATT_CONNECTING =
            "com.example.bluetooth.le.ACTION_GATT_CONNECTING";
    public final static String ACTION_GATT_DISCONNECTED =
            "com.example.bluetooth.le.ACTION_GATT_DISCONNECTED";
    public final static String ACTION_GATT_DISCONNECTED_CAROUSEL =
            "com.example.bluetooth.le.ACTION_GATT_DISCONNECTED_CAROUSEL";
    public final static String ACTION_GATT_SERVICES_DISCOVERED =
            "com.example.bluetooth.le.ACTION_GATT_SERVICES_DISCOVERED";
    public final static String ACTION_DATA_AVAILABLE =
            "com.example.bluetooth.le.ACTION_DATA_AVAILABLE";
    public final static String ACTION_OTA_DATA_AVAILABLE =
            "com.cysmart.bluetooth.le.ACTION_OTA_DATA_AVAILABLE";
    public final static String ACTION_GATT_DISCONNECTED_OTA =
            "com.example.bluetooth.le.ACTION_GATT_DISCONNECTED_OTA";
    public final static String ACTION_GATT_CONNECT_OTA =
            "com.example.bluetooth.le.ACTION_GATT_CONNECT_OTA";
    public final static String ACTION_GATT_SERVICES_DISCOVERED_OTA =
            "com.example.bluetooth.le.ACTION_GATT_SERVICES_DISCOVERED_OTA";
    public final static String ACTION_GATT_CHARACTERISTIC_ERROR =
            "com.example.bluetooth.le.ACTION_GATT_CHARACTERISTIC_ERROR";
    public final static String ACTION_GATT_SERVICE_DISCOVERY_UNSUCCESSFUL =
            "com.example.bluetooth.le.ACTION_GATT_SERVICE_DISCOVERY_UNSUCCESSFUL";
    public final static String ACTION_PAIR_REQUEST =
            "android.bluetooth.device.action.PAIRING_REQUEST";
    public final static String ACTION_WRITE_COMPLETED =
            "android.bluetooth.device.action.ACTION_WRITE_COMPLETED";
    public final static String ACTION_WRITE_FAILED =
            "android.bluetooth.device.action.ACTION_WRITE_FAILED";
    public final static String ACTION_WRITE_SUCCESS =
            "android.bluetooth.device.action.ACTION_WRITE_SUCCESS";
    /**
     * Connection status Constants
     */
    public static final int STATE_DISCONNECTED = 0;
    public static final int STATE_CONNECTING = 1;
    public static final int STATE_CONNECTED = 2;
    public static final int STATE_DISCONNECTING = 4;
    private final static String ACTION_GATT_DISCONNECTING =
            "com.example.bluetooth.le.ACTION_GATT_DISCONNECTING";

    /**
     * BluetoothAdapter for handling connections
     */
    public static BluetoothAdapter mBluetoothAdapter;
    public static BluetoothGatt mBluetoothGatt;
    /**
     * Disable?enable notification
     */
    public static ArrayList<BluetoothGattCharacteristic> mEnabledCharacteristics =
            new ArrayList<BluetoothGattCharacteristic>();
    public static ArrayList<BluetoothGattCharacteristic> mRDKCharacteristics =
            new ArrayList<BluetoothGattCharacteristic>();
    public static ArrayList<BluetoothGattCharacteristic> mGlucoseCharacteristics =
            new ArrayList<BluetoothGattCharacteristic>();
    public static boolean mDisableNotificationFlag = false;
    public static boolean mEnableRDKNotificationFlag = false;

    public static boolean mEnableGlucoseFlag = false;


    private static int mConnectionState = STATE_DISCONNECTED;
    private static boolean mOtaExitBootloaderCmdInProgress = false;
    /**
     * Device address
     */
    private static String mBluetoothDeviceAddress;
    private static String mBluetoothDeviceName;
    private static Context mContext;
    private static OnDataAvailableListener mOnDataAvailableListener;

    public interface OnDataAvailableListener {
        public void onCharacteristicRead(BluetoothGatt gatt,
                                         BluetoothGattCharacteristic characteristic, int status);

        public void onCharacteristicWrite(BluetoothGatt gatt,
                                          BluetoothGattCharacteristic characteristic);

        public void onCharacteristicChanged(BluetoothGatt gatt,
                                            BluetoothGattCharacteristic characteristic);
    }



    /**
     * Implements callback methods for GATT events that the app cares about. For
     * example,connection change and services discovered.
     */
    private final static BluetoothGattCallback mGattCallback = new BluetoothGattCallback() {
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status,
                                            int newState) {
            Logger.i("onConnectionStateChange");
            String intentAction;
            // GATT Server connected
            if (newState == BluetoothProfile.STATE_CONNECTED) {
                intentAction = ACTION_GATT_CONNECTED;
                synchronized (mGattCallback) {
                    mConnectionState = STATE_CONNECTED;
                }
                broadcastConnectionUpdate(intentAction);
                Logger.d(TAG, "[GATT Server connected-" + mBluetoothDeviceName + "|" + mBluetoothDeviceAddress + "] ");

            }
            // GATT Server disconnected
            else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                intentAction = ACTION_GATT_DISCONNECTED;
                synchronized (mGattCallback) {
                    mConnectionState = STATE_DISCONNECTED;
                }
                broadcastConnectionUpdate(intentAction);
                Logger.d(TAG, "[GATT Server disconnected-" + mBluetoothDeviceName + "|" + mBluetoothDeviceAddress + "] ");
            }
            // GATT Server Connecting
            if (newState == BluetoothProfile.STATE_CONNECTING) {
                intentAction = ACTION_GATT_CONNECTING;
                synchronized (mGattCallback) {
                    mConnectionState = STATE_CONNECTING;
                }
                broadcastConnectionUpdate(intentAction);
                Logger.d(TAG, "[GATT Server Connecting-" + mBluetoothDeviceName + "|" + mBluetoothDeviceAddress + "] ");
            }
            // GATT Server disconnected
            else if (newState == BluetoothProfile.STATE_DISCONNECTING) {
                intentAction = ACTION_GATT_DISCONNECTING;
                synchronized (mGattCallback) {
                    mConnectionState = STATE_DISCONNECTING;
                }
                broadcastConnectionUpdate(intentAction);
            }
        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            // GATT Services discovered
            if (status == BluetoothGatt.GATT_SUCCESS) {
                Logger.d(TAG, "[GATT Services discovered-" + mBluetoothDeviceName + "|" + mBluetoothDeviceAddress + "] ");
                broadcastConnectionUpdate(ACTION_GATT_SERVICES_DISCOVERED);
            } else if (status == BluetoothGatt.GATT_INSUFFICIENT_AUTHENTICATION ||
                    status == BluetoothGatt.GATT_INSUFFICIENT_ENCRYPTION) {
                bondDevice();
                broadcastConnectionUpdate(ACTION_GATT_SERVICE_DISCOVERY_UNSUCCESSFUL);
            } else {
                Logger.d(TAG, "[" + mBluetoothDeviceName + "|" + mBluetoothDeviceAddress + "] ");
                broadcastConnectionUpdate(ACTION_GATT_SERVICE_DISCOVERY_UNSUCCESSFUL);
            }
        }

        @Override
        public void onDescriptorWrite(BluetoothGatt gatt, BluetoothGattDescriptor descriptor,
                                      int status) {
            String serviceUUID = descriptor.getCharacteristic().getService().getUuid().toString();
            String serviceName = GattAttributes.lookupUUID(descriptor.getCharacteristic().
                    getService().getUuid(), serviceUUID);

            String characteristicUUID = descriptor.getCharacteristic().getUuid().toString();
            String characteristicName = GattAttributes.lookupUUID(descriptor.getCharacteristic().
                    getUuid(), characteristicUUID);

            String descriptorUUID = descriptor.getUuid().toString();
            String descriptorName = GattAttributes.lookupUUID(descriptor.getUuid(), descriptorUUID);

            if (status == BluetoothGatt.GATT_SUCCESS) {
                Intent intent = new Intent(ACTION_WRITE_SUCCESS);
                mContext.sendBroadcast(intent);
                Logger.d(TAG, "[BluetoothGatt.GATT_SUCCESS-" + serviceName + "|" + characteristicName + "|" + descriptorName + "] ");
                if (descriptor.getValue() != null)
                    addRemoveData(descriptor);
                if (mDisableNotificationFlag) {
                    disableAllEnabledCharacteristics();
                } else if (mEnableRDKNotificationFlag) {
                    if (mRDKCharacteristics.size() > 0) {
                        mRDKCharacteristics.remove(0);
                        enableAllRDKCharacteristics();
                    }

                } else if (mEnableGlucoseFlag) {
                    if (mGlucoseCharacteristics.size() > 0) {
                        mGlucoseCharacteristics.remove(0);
                        enableAllGlucoseCharacteristics();
                    }

                }
            } else if (status == BluetoothGatt.GATT_INSUFFICIENT_AUTHENTICATION
                    || status == BluetoothGatt.GATT_INSUFFICIENT_ENCRYPTION) {
                bondDevice();
                Intent intent = new Intent(ACTION_WRITE_FAILED);
                mContext.sendBroadcast(intent);
            } else {

                Logger.d(TAG, "[" + serviceName + "|" + characteristicName + "|" + descriptorName + "] " + status);
                mDisableNotificationFlag = false;
                mEnableRDKNotificationFlag = false;
                mEnableGlucoseFlag = false;
                Intent intent = new Intent(ACTION_WRITE_FAILED);
                mContext.sendBroadcast(intent);
            }
        }


        @Override
        public void onDescriptorRead(BluetoothGatt gatt, BluetoothGattDescriptor descriptor,
                                     int status) {
            String serviceUUID = descriptor.getCharacteristic().getService().getUuid().toString();
            String serviceName = GattAttributes.lookupUUID(descriptor.getCharacteristic().getService().getUuid(), serviceUUID);

            String characteristicUUID = descriptor.getCharacteristic().getUuid().toString();
            String characteristicName = GattAttributes.lookupUUID(descriptor.getCharacteristic().getUuid(), characteristicUUID);

            String descriptorUUIDText = descriptor.getUuid().toString();
            String descriptorName = GattAttributes.lookupUUID(descriptor.getUuid(), descriptorUUIDText);

            String descriptorValue = " " + Utils.ByteArraytoHex(descriptor.getValue()) + " ";
            if (status == BluetoothGatt.GATT_SUCCESS) {
                UUID descriptorUUID = descriptor.getUuid();
                final Intent intent = new Intent(ACTION_DATA_AVAILABLE);
                Bundle mBundle = new Bundle();
                // Putting the byte value read for GATT Db
                mBundle.putByteArray(Constants.EXTRA_DESCRIPTOR_BYTE_VALUE,
                        descriptor.getValue());
                mBundle.putInt(Constants.EXTRA_BYTE_DESCRIPTOR_INSTANCE_VALUE,
                        descriptor.getCharacteristic().getInstanceId());

                Logger.d(TAG, "[" + serviceName + "|" + characteristicName + "|" + descriptorName + "] " +
                        "[" + descriptorValue + "]");
                mBundle.putString(Constants.EXTRA_DESCRIPTOR_BYTE_VALUE_UUID,
                        descriptor.getUuid().toString());
                mBundle.putString(Constants.EXTRA_DESCRIPTOR_BYTE_VALUE_CHARACTERISTIC_UUID,
                        descriptor.getCharacteristic().getUuid().toString());
                if (descriptorUUID.equals(UUIDDatabase.UUID_CLIENT_CHARACTERISTIC_CONFIG)) {

                    mBundle.putString(Constants.EXTRA_DESCRIPTOR_VALUE, "1");
                } else if (descriptorUUID.equals(UUIDDatabase.UUID_CHARACTERISTIC_EXTENDED_PROPERTIES)) {

                    mBundle.putString(Constants.EXTRA_DESCRIPTOR_VALUE, "2");
                } else if (descriptorUUID.equals(UUIDDatabase.UUID_CHARACTERISTIC_USER_DESCRIPTION)) {

                    mBundle.putString(Constants.EXTRA_DESCRIPTOR_VALUE, "3");
                } else if (descriptorUUID.equals(UUIDDatabase.UUID_SERVER_CHARACTERISTIC_CONFIGURATION)) {

                    mBundle.putString(Constants.EXTRA_DESCRIPTOR_VALUE, "4");
                } else if (descriptorUUID.equals(UUIDDatabase.UUID_REPORT_REFERENCE)) {


                } else if (descriptorUUID.equals(UUIDDatabase.UUID_CHARACTERISTIC_PRESENTATION_FORMAT)) {

                    mBundle.putString(Constants.EXTRA_DESCRIPTOR_VALUE,
                            "5");
                }
                intent.putExtras(mBundle);
                /**
                 * Sending the broad cast so that it can be received on
                 * registered receivers
                 */
                mContext.sendBroadcast(intent);
            } else {

            }

        }

        @Override
        public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic
                characteristic, int status) {
            String serviceUUID = characteristic.getService().getUuid().toString();
            String serviceName = GattAttributes.lookupUUID(characteristic.getService().getUuid(), serviceUUID);

            String characteristicUUID = characteristic.getUuid().toString();
            String characteristicName = GattAttributes.lookupUUID(characteristic.getUuid(), characteristicUUID);

            String dataLog = "";
            if (status == BluetoothGatt.GATT_SUCCESS) {

            } else {

                Intent intent = new Intent(ACTION_GATT_CHARACTERISTIC_ERROR);
                intent.putExtra(Constants.EXTRA_CHARACTERISTIC_ERROR_MESSAGE, "" + status);
                mContext.sendBroadcast(intent);

            }

            Logger.d("CYSMART", dataLog);
            boolean isExitBootloaderCmd = false;
            synchronized (mGattCallback) {
                isExitBootloaderCmd = mOtaExitBootloaderCmdInProgress;
                if (mOtaExitBootloaderCmdInProgress)
                    mOtaExitBootloaderCmdInProgress = false;
            }

            if (isExitBootloaderCmd)
                onOtaExitBootloaderComplete(status);

            if (mOnDataAvailableListener != null)
                mOnDataAvailableListener.onCharacteristicWrite(gatt,
                        characteristic);
        }

        @Override
        public void onCharacteristicRead(BluetoothGatt gatt,
                                         BluetoothGattCharacteristic characteristic, int status) {
            String serviceUUID = characteristic.getService().getUuid().toString();
            String serviceName = GattAttributes.lookupUUID(characteristic.getService().getUuid(), serviceUUID);

            String characteristicUUID = characteristic.getUuid().toString();
            String characteristicName = GattAttributes.lookupUUID(characteristic.getUuid(), characteristicUUID);

            String characteristicValue = " " + Utils.ByteArraytoHex(characteristic.getValue()) + " ";
            // GATT Characteristic read
            if (status == BluetoothGatt.GATT_SUCCESS) {
                broadcastNotifyUpdate(characteristic);
            } else {
                if (status == BluetoothGatt.GATT_INSUFFICIENT_AUTHENTICATION
                        || status == BluetoothGatt.GATT_INSUFFICIENT_ENCRYPTION) {
                    bondDevice();
                }
            }
            if (mOnDataAvailableListener != null)
                mOnDataAvailableListener.onCharacteristicRead(gatt,
                        characteristic, status);
        }

        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt,
                                            BluetoothGattCharacteristic characteristic) {
            String serviceUUID = characteristic.getService().getUuid().toString();
            String serviceName = GattAttributes.lookupUUID(characteristic.getService().getUuid(), serviceUUID);

            String characteristicUUID = characteristic.getUuid().toString();
            String characteristicName = GattAttributes.lookupUUID(characteristic.getUuid(), characteristicUUID);

            String characteristicValue = Utils.ByteArraytoHex(characteristic.getValue());

            broadcastNotifyUpdate(characteristic);
            if (mOnDataAvailableListener != null)
                mOnDataAvailableListener.onCharacteristicChanged(gatt,
                        characteristic);
        }

        @Override
        public void onMtuChanged(BluetoothGatt gatt, int mtu, int status) {
            Resources res = mContext.getResources();

        }
    };




    private final IBinder mBinder = new LocalBinder();
    /**
     * Flag to check the mBound status
     */
    public boolean mBound;
    /**
     * BlueTooth manager for handling connections
     */
    private BluetoothManager mBluetoothManager;



    private static void broadcastConnectionUpdate(final String action) {
        Logger.i("action :" + action);
        final Intent intent = new Intent(action);
        mContext.sendBroadcast(intent);
    }

    private static void broadcastWritwStatusUpdate(final String action) {
        final Intent intent = new Intent((action));
        mContext.sendBroadcast(intent);
    }

    private static void broadcastNotifyUpdate(final BluetoothGattCharacteristic characteristic) {
        final Intent intent = new Intent(BluetoothLeService.ACTION_DATA_AVAILABLE);
        Bundle mBundle = new Bundle();
        // Putting the byte value read for GATT Db
        mBundle.putByteArray(Constants.EXTRA_BYTE_VALUE,
                characteristic.getValue());
        mBundle.putString(Constants.EXTRA_BYTE_UUID_VALUE,
                characteristic.getUuid().toString());
        mBundle.putInt(Constants.EXTRA_BYTE_INSTANCE_VALUE,
                characteristic.getInstanceId());
        mBundle.putString(Constants.EXTRA_BYTE_SERVICE_UUID_VALUE,
                characteristic.getService().getUuid().toString());
        mBundle.putInt(Constants.EXTRA_BYTE_SERVICE_INSTANCE_VALUE,
                characteristic.getService().getInstanceId());

        if (characteristic.getUuid().equals(UUIDDatabase.UUID_HEART_RATE_MEASUREMENT)) {


        }

        else if (characteristic.getUuid().equals(UUIDDatabase.UUID_HEALTH_THERMOMETER)) {


        }

        else if (characteristic.getUuid().equals(UUIDDatabase.UUID_BLOOD_PRESSURE_MEASUREMENT)) {

            mBundle.putString(
                    Constants.EXTRA_PRESURE_SYSTOLIC_UNIT_VALUE,
                    "15");
            mBundle.putString(
                    Constants.EXTRA_PRESURE_DIASTOLIC_UNIT_VALUE,
                    "16");
            mBundle.putString(
                    Constants.EXTRA_PRESURE_SYSTOLIC_VALUE,
                    "17");
            mBundle.putString(
                    Constants.EXTRA_PRESURE_DIASTOLIC_VALUE,
                    "18");

        }

        else if (characteristic.getUuid().equals(UUIDDatabase.UUID_CSC_MEASURE)) {


        }

        else if (characteristic.getUuid().equals(UUIDDatabase.UUID_REP0RT)) {
            BluetoothGattDescriptor descriptor = characteristic.getDescriptor
                    (UUIDDatabase.UUID_REPORT_REFERENCE);
            if (descriptor != null) {
                BluetoothLeService.readDescriptor(characteristic.getDescriptor(
                        UUIDDatabase.UUID_REPORT_REFERENCE));


                mBundle.putString(Constants.EXTRA_DESCRIPTOR_REPORT_REFERENCE_ID,
                        "19");
                mBundle.putString(Constants.EXTRA_DESCRIPTOR_REPORT_REFERENCE_TYPE,
                        "20");
            }
        }
        mContext.sendBroadcast(intent);
    }


    private static void onOtaExitBootloaderComplete(int status) {
        Bundle bundle = new Bundle();
        bundle.putByteArray(Constants.EXTRA_BYTE_VALUE, new byte[]{(byte) status});
        Intent intentOTA = new Intent(BluetoothLeService.ACTION_OTA_DATA_AVAILABLE);
        intentOTA.putExtras(bundle);
        mContext.sendBroadcast(intentOTA);
    }



    /**
     * Request a read on a given {@code BluetoothGattDescriptor }.
     *
     * @param descriptor The descriptor to read from.
     */
    public static void readDescriptor(
            BluetoothGattDescriptor descriptor) {
        String serviceUUID = descriptor.getCharacteristic().getService().getUuid().toString();
        String serviceName = GattAttributes.lookupUUID(descriptor.getCharacteristic().getService().getUuid(), serviceUUID);

        String characteristicUUID = descriptor.getCharacteristic().getUuid().toString();
        String characteristicName = GattAttributes.lookupUUID(descriptor.getCharacteristic().getUuid(), characteristicUUID);
        if (mBluetoothAdapter == null || mBluetoothGatt == null) {
            return;
        }

        mBluetoothGatt.readDescriptor(descriptor);

    }

    /**
     * Request a write with no response on a given
     * {@code BluetoothGattCharacteristic}.
     *
     * @param characteristic
     * @param byteArray      to write
     */


    /**
     * Enables or disables notification on a give characteristic.
     *
     * @param characteristic Characteristic to act on.
     * @param enabled        If true, enable notification. False otherwise.
     */
    public static void setCharacteristicNotification(
            BluetoothGattCharacteristic characteristic, boolean enabled) {
        String serviceUUID = characteristic.getService().getUuid().toString();
        String serviceName = GattAttributes.lookupUUID(characteristic.getService().getUuid(), serviceUUID);

        String characteristicUUID = characteristic.getUuid().toString();
        String characteristicName = GattAttributes.lookupUUID(characteristic.getUuid(), characteristicUUID);

        String descriptorUUID = GattAttributes.CLIENT_CHARACTERISTIC_CONFIG;
        String descriptorName = GattAttributes.lookupUUID(UUIDDatabase.
                UUID_CLIENT_CHARACTERISTIC_CONFIG, descriptorUUID);
        if (mBluetoothAdapter == null || mBluetoothGatt == null) {
            return;
        }
        if (characteristic.getDescriptor(UUID
                .fromString(GattAttributes.CLIENT_CHARACTERISTIC_CONFIG)) != null) {
            if (enabled == true) {
                BluetoothGattDescriptor descriptor = characteristic
                        .getDescriptor(UUID
                                .fromString(GattAttributes.CLIENT_CHARACTERISTIC_CONFIG));
                descriptor
                        .setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
                mBluetoothGatt.writeDescriptor(descriptor);


            } else {
                BluetoothGattDescriptor descriptor = characteristic
                        .getDescriptor(UUID
                                .fromString(GattAttributes.CLIENT_CHARACTERISTIC_CONFIG));
                descriptor
                        .setValue(BluetoothGattDescriptor.DISABLE_NOTIFICATION_VALUE);
                mBluetoothGatt.writeDescriptor(descriptor);

            }
        }
        mBluetoothGatt.setCharacteristicNotification(characteristic, enabled);
        if (enabled) {

        } else {

        }

    }

    /**
     * Enables or disables indications on a give characteristic.
     *
     * @param characteristic Characteristic to act on.
     * @param enabled        If true, enable indications. False otherwise.
     */
    public static void setCharacteristicIndication(
            BluetoothGattCharacteristic characteristic, boolean enabled) {
        String serviceUUID = characteristic.getService().getUuid().toString();
        String serviceName = GattAttributes.lookupUUID(characteristic.getService().getUuid(),
                serviceUUID);

        String characteristicUUID = characteristic.getUuid().toString();
        String characteristicName = GattAttributes.lookupUUID(characteristic.getUuid(),
                characteristicUUID);

        String descriptorUUID = GattAttributes.CLIENT_CHARACTERISTIC_CONFIG;
        String descriptorName = GattAttributes.lookupUUID(UUIDDatabase.
                UUID_CLIENT_CHARACTERISTIC_CONFIG, descriptorUUID);
        if (mBluetoothAdapter == null || mBluetoothGatt == null) {
            return;
        }

        if (characteristic.getDescriptor(UUID
                .fromString(GattAttributes.CLIENT_CHARACTERISTIC_CONFIG)) != null) {
            if (enabled == true) {
                BluetoothGattDescriptor descriptor = characteristic
                        .getDescriptor(UUID
                                .fromString(GattAttributes.CLIENT_CHARACTERISTIC_CONFIG));
                descriptor
                        .setValue(BluetoothGattDescriptor.ENABLE_INDICATION_VALUE);
                mBluetoothGatt.writeDescriptor(descriptor);

            } else {
                BluetoothGattDescriptor descriptor = characteristic
                        .getDescriptor(UUID
                                .fromString(GattAttributes.CLIENT_CHARACTERISTIC_CONFIG));
                descriptor
                        .setValue(BluetoothGattDescriptor.DISABLE_NOTIFICATION_VALUE);
                mBluetoothGatt.writeDescriptor(descriptor);

            }
        }
        mBluetoothGatt.setCharacteristicNotification(characteristic, enabled);
        if (enabled) {

        } else {

        }
    }

    /**
     * Retrieves a list of supported GATT services on the connected device. This
     * should be invoked only after {@code BluetoothGatt#discoverServices()}
     * completes successfully.
     *
     * @return A {@code List} of supported services.
     */
    public static List<BluetoothGattService> getSupportedGattServices() {
        if (mBluetoothGatt == null)
            return null;

        return mBluetoothGatt.getServices();
    }



    public static void bondDevice() {
        try {
            Class class1 = Class.forName("android.bluetooth.BluetoothDevice");
            Method createBondMethod = class1.getMethod("createBond");
            Boolean returnValue = (Boolean) createBondMethod.invoke(mBluetoothGatt.getDevice());
            Logger.e("Pair initates status-->" + returnValue);
        } catch (Exception e) {
            Logger.e("Exception Pair" + e.getMessage());
        }

    }

    public static void addRemoveData(BluetoothGattDescriptor descriptor) {
        switch (descriptor.getValue()[0]) {
            case 0:
                //Disabled notification and indication
                removeEnabledCharacteristic(descriptor.getCharacteristic());
                Logger.e("Removed characteristic");
                break;
            case 1:
                //Enabled notification
                Logger.e("added notify characteristic");
                addEnabledCharacteristic(descriptor.getCharacteristic());
                break;
            case 2:
                //Enabled indication
                Logger.e("added indicate characteristic");
                addEnabledCharacteristic(descriptor.getCharacteristic());
                break;
        }
    }

    public static void addEnabledCharacteristic(BluetoothGattCharacteristic
                                                        bluetoothGattCharacteristic) {
        if (!mEnabledCharacteristics.contains(bluetoothGattCharacteristic))
            mEnabledCharacteristics.add(bluetoothGattCharacteristic);
    }

    public static void removeEnabledCharacteristic(BluetoothGattCharacteristic
                                                           bluetoothGattCharacteristic) {
        if (mEnabledCharacteristics.contains(bluetoothGattCharacteristic))
            mEnabledCharacteristics.remove(bluetoothGattCharacteristic);
    }

    public static void disableAllEnabledCharacteristics() {
        if (mEnabledCharacteristics.size() > 0) {
            mDisableNotificationFlag = true;
            BluetoothGattCharacteristic bluetoothGattCharacteristic = mEnabledCharacteristics.
                    get(0);
            Logger.e("Disabling characteristic--" + bluetoothGattCharacteristic.getUuid());
            setCharacteristicNotification(bluetoothGattCharacteristic, false);
        } else {
            mDisableNotificationFlag = false;
        }

    }

    public static void enableAllRDKCharacteristics() {
        if (mRDKCharacteristics.size() > 0) {
            mEnableRDKNotificationFlag = true;
            BluetoothGattCharacteristic bluetoothGattCharacteristic = mRDKCharacteristics.
                    get(0);
            Logger.e("enabling characteristic--" + bluetoothGattCharacteristic.getInstanceId());
            setCharacteristicNotification(bluetoothGattCharacteristic, true);
        } else {
            Logger.e("All RDK Chara enabled");
            mEnableRDKNotificationFlag = false;
            String intentAction = ACTION_WRITE_COMPLETED;
            broadcastWritwStatusUpdate(intentAction);
        }

    }

    public static void enableAllGlucoseCharacteristics() {
        if (mGlucoseCharacteristics.size() > 0) {
            mEnableGlucoseFlag = true;
            BluetoothGattCharacteristic bluetoothGattCharacteristic = mGlucoseCharacteristics.
                    get(0);
            Logger.e("enabling characteristic--" + bluetoothGattCharacteristic);
            if (bluetoothGattCharacteristic.getUuid().equals(UUIDDatabase.UUID_RECORD_ACCESS_CONTROL_POINT)) {
                setCharacteristicIndication(bluetoothGattCharacteristic, true);
                Logger.e("RACP Indicate");
            } else {
                setCharacteristicNotification(bluetoothGattCharacteristic, true);
            }
        } else {
            Logger.e("All Gluocse Char enabled");
            mEnableGlucoseFlag = false;
            String intentAction = ACTION_WRITE_COMPLETED;
            broadcastWritwStatusUpdate(intentAction);
        }
    }

    /**
     * After using a given BLE device, the app must call this method to ensure
     * resources are released properly.
     */
    public static void close() {
        mBluetoothGatt.close();
        mBluetoothGatt = null;
        Logger.e("close");
    }

    @Override
    public IBinder onBind(Intent intent) {
        mBound = true;
        return mBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        mBound = false;
        close();
        return super.onUnbind(intent);
    }

    /**
     * Initializes a reference to the local BlueTooth adapter.
     *
     * @return Return true if the initialization is successful.
     */
    public boolean initialize() {
        // For API level 18 and above, get a reference to BluetoothAdapter
        // through
        // BluetoothManager.
        if (mBluetoothManager == null) {
            mBluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
            if (mBluetoothManager == null) {
                return false;
            }
        }

        mBluetoothAdapter = mBluetoothManager.getAdapter();
        return mBluetoothAdapter != null;

    }

    /**
     * lite单例模式
     */
    private volatile static LiteBluetooth liteBluetooth;

    public static LiteBluetooth getInstance(Context context) {
        if (liteBluetooth == null) {
            synchronized (LiteBluetooth.class) {
                if (liteBluetooth == null) {
                    liteBluetooth = new LiteBluetooth(context);
                }
            }
        }
        return liteBluetooth;
    }

    @Override
    public void onCreate() {
        // Initializing the service
        liteBluetooth = getInstance(this);
        dbManager = new DBManager(this);
        dbManager.getMySwitchList();

        if (!initialize()) {
            Logger.d("Service not initialized");
        }
    }


    public int onStartCommand(Intent intent, int flags, int startId) {
        // TODO Auto-generated method stub

        if (intent != null) {
            String cmd = intent.getStringExtra("cmd");
            String para1 = intent.getStringExtra("para1");
            String para2 = intent.getStringExtra("para2");
            String para3 = intent.getStringExtra("para3");
            String para4 = intent.getStringExtra("para4");
            Log.i(TAG, "cmd:"+cmd);
            Log.i(TAG, "para1:"+para1);
            Log.i(TAG, "para2:"+para2);
            Log.i(TAG, "para3:"+para3);
            Log.i(TAG, "para4:"+para4);
            dealcmd(cmd, para1, para2, para3, para4);
        }

        return super.onStartCommand(intent, flags, startId);
    }

    private void dealcmd(String cmd, String para1, String para2, String para3, String para4) {

        if(cmd.equals("TJ")){
            MySwitch mySwitch = new MySwitch(para3);
            mySwitch.setSwitchRssi(para4);
            mySwitch.setBlueMac(para1);
            MyList.settingList.add(mySwitch);
            dbManager.setMySwitchList();
            sendmessage(cmd, para1, para2, para3, para4);
        }
        if(cmd.equals("SX")){
            scanDevicesPeriod();
            sendmessage("SX", "", "", "", "");
        }
        if(cmd.equals("XG")){
            sendmessage("XG",para1,para2,para3,para4);
        }
        if(cmd.equals("SZ")){
            sendmessage("SZ",para1,para2,para3,para4);
        }
    }

    /**
     * Local binder class
     */
    public class LocalBinder extends Binder {
        public BluetoothLeService getService() {
            return BluetoothLeService.this;
        }
    }


    //搜索蓝牙设备
    private void scanDevicesPeriod() {
        liteBluetooth.startLeScan(new PeriodScanCallback(TIME_OUT_SCAN) {
            @Override
            public void onScanTimeout() {
                // dialogShow(TIME_OUT_SCAN + " Millis Scan Timeout! ");
            }

            @Override
            public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
                for(int i = 0;i<MyList.settingList.size();i++){
                    if(device.getAddress().equals(MyList.settingList.get(i).getBlueMac())){
                        MyList.settingList.get(i).setSwitchRssi(rssi+"");
                        break;
                    }
                }

            }
        });
    }

    private void sendmessage(String cmd, String para1, String para2,
                             String para3, String para4) {
        Intent in = new Intent();
        in.putExtra("cmd", cmd);
        in.putExtra("para1", para1);
        in.putExtra("para2", para2);
        in.putExtra("para3", para3);
        in.putExtra("para4", para4);
        in.setAction(SERVICEACTION);
        sendBroadcast(in);
    }
}