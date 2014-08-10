package com.lovebuzz;

import java.util.ArrayList;
import java.util.List;

import android.app.ListActivity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;

/**
 * Activity for scanning and displaying available BLE devices.
 */
public class DeviceScanActivity extends ListActivity {

  private BluetoothAdapter mBluetoothAdapter;
  private boolean mScanning;
  private Handler mHandler;

  private LeDeviceListAdapter mLeDeviceListAdapter;

  // Stops scanning after 10 seconds.
  private static final long SCAN_PERIOD = 10000;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_device_scan);
    setListAdapter(mLeDeviceListAdapter);
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.main, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.
    int id = item.getItemId();
    if (id == R.id.action_settings) {
      return true;
    }
    return super.onOptionsItemSelected(item);
  }

  private void scanLeDevice(final boolean enable) {
    if (enable) {
      // Stops scanning after a pre-defined scan period.
      mHandler.postDelayed(new Runnable() {
        @Override
        public void run() {
          mScanning = false;
          mBluetoothAdapter.stopLeScan(mLeScanCallback);
        }
      }, SCAN_PERIOD);

      mScanning = true;
      mBluetoothAdapter.startLeScan(mLeScanCallback);
    } else {
      mScanning = false;
      mBluetoothAdapter.stopLeScan(mLeScanCallback);
    }
  }

  // Device scan callback.
  private BluetoothAdapter.LeScanCallback mLeScanCallback = new BluetoothAdapter.LeScanCallback() {

    @Override
    public void onLeScan(final BluetoothDevice device, int rssi, byte[] scanRecord) {
      runOnUiThread(new Runnable() {
        @Override
        public void run() {
          mLeDeviceListAdapter.addDevice(device);
          mLeDeviceListAdapter.notifyDataSetChanged();
        }
      });
    }
  };

  class LeDeviceListAdapter implements ListAdapter {
    List<BluetoothDevice> devices = new ArrayList<BluetoothDevice>();

    public void addDevice(BluetoothDevice device) {
      devices.add(device);
    }

    public void notifyDataSetChanged() {
      // TODO Auto-generated method stub

    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {
      // TODO Auto-generated method stub

    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {
      // TODO Auto-generated method stub

    }

    @Override
    public int getCount() {
      // TODO Auto-generated method stub
      return devices.size();
    }

    @Override
    public Object getItem(int position) {
      if (devices.size() > position) {
        return devices.get(position);
      }
      return null;
    }

    @Override
    public long getItemId(int position) {
      // TODO Auto-generated method stub
      return 0;
    }

    @Override
    public boolean hasStableIds() {
      // TODO Auto-generated method stub
      return false;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
      // TODO Auto-generated method stub
      return null;
    }

    @Override
    public int getItemViewType(int position) {
      // TODO Auto-generated method stub
      return 0;
    }

    @Override
    public int getViewTypeCount() {
      // TODO Auto-generated method stub
      return 0;
    }

    @Override
    public boolean isEmpty() {
      return devices.isEmpty();
    }

    @Override
    public boolean areAllItemsEnabled() {
      // TODO Auto-generated method stub
      return false;
    }

    @Override
    public boolean isEnabled(int position) {
      // TODO Auto-generated method stub
      return false;
    }

  }
}
