package cz.nitramek.linewarriors.networking;


import android.content.Context;
import android.net.nsd.NsdManager;
import android.net.nsd.NsdServiceInfo;
import android.util.Log;

import java.net.InetAddress;
import java.net.InetSocketAddress;

public class NsdHelper {

    public static final String TAG = NsdHelper.class.getName();
    public static final String SERVICE_NAME = "nitramek_LineWarriors";
    public static final String SERVICE_TYPE = "_http._tcp.";
    private NsdManager.DiscoveryListener discoveryListener;
    private NsdManager.RegistrationListener registrationListener;

    private boolean registred = false;

    private boolean alreadyFound = false;
    private NsdManager manager;
    private ServiceListener serviceListener;
    private NsdManager.ResolveListener resolveListener;

    private String thisDeviceServicename = "";
    //TODO spojení se nezdařílo

    public NsdHelper(Context context, ServiceListener serviceListener) {
        this.serviceListener = serviceListener;
        this.discoveryListener = new NsdManager.DiscoveryListener() {
            @Override
            public void onStartDiscoveryFailed(String serviceType, int errorCode) {
                Log.d(TAG, "onStartDiscoveryFailed");
            }

            @Override
            public void onStopDiscoveryFailed(String serviceType, int errorCode) {
                Log.d(TAG, "onStopDiscoveryFailed");
            }

            @Override
            public void onDiscoveryStarted(String serviceType) {
                Log.d(TAG, "onDiscoveryStarted");
            }

            @Override
            public void onDiscoveryStopped(String serviceType) {
                Log.d(TAG, "onDiscoveryStopped");
            }

            @Override
            public void onServiceFound(NsdServiceInfo serviceInfo) {
                Log.d(TAG, "onServiceFound");
                //found second service
                final String serviceName = serviceInfo.getServiceName();
                if (serviceName.contains(SERVICE_NAME) && !alreadyFound) {
                    NsdHelper.this.manager.resolveService(serviceInfo, resolveListener);
                    alreadyFound = true;
                }
            }

            @Override
            public void onServiceLost(NsdServiceInfo serviceInfo) {
                Log.d(TAG, "onServiceLost");
                if (serviceInfo.getServiceName().equals(SERVICE_NAME)) {
                    NsdHelper.this.serviceListener.stop();
                }
            }
        };
        this.registrationListener = new NsdManager.RegistrationListener() {
            @Override
            public void onRegistrationFailed(NsdServiceInfo serviceInfo, int errorCode) {
                Log.d(TAG, "onRegistrationFailed");
            }

            @Override
            public void onUnregistrationFailed(NsdServiceInfo serviceInfo, int errorCode) {
                Log.d(TAG, "onUnregistrationFailed");
            }

            @Override
            public void onServiceRegistered(NsdServiceInfo serviceInfo) {
                Log.d(TAG, "onServiceRegistered");
                thisDeviceServicename = serviceInfo.getServiceName();
            }

            @Override
            public void onServiceUnregistered(NsdServiceInfo serviceInfo) {
                Log.d(TAG, "onServiceUnregistered");
            }
        };
        this.resolveListener = new NsdManager.ResolveListener() {
            @Override
            public void onResolveFailed(NsdServiceInfo serviceInfo, int errorCode) {
                Log.d(TAG, "onResolveFailed");
            }

            @Override
            public void onServiceResolved(NsdServiceInfo serviceInfo) {
                Log.i(TAG, String.format("onServiceResolved %s", serviceInfo.toString()));
                //I dont reslove any other
                alreadyFound = true;
                NsdHelper.this.serviceListener.connectTo(serviceInfo.getHost(), serviceInfo.getPort());
            }
        };
        manager = (NsdManager) context.getSystemService(Context.NSD_SERVICE);
    }

    public void registerService() {
        if (!registred) {
            NsdServiceInfo nsdInfo = new NsdServiceInfo();
            nsdInfo.setServiceName(SERVICE_NAME);
            nsdInfo.setServiceType(SERVICE_TYPE);
            final InetSocketAddress inetSocketAddress = this.serviceListener.startAsServer();
            nsdInfo.setPort(inetSocketAddress.getPort());
            nsdInfo.setHost(inetSocketAddress.getAddress());
            this.manager.registerService(nsdInfo, NsdManager.PROTOCOL_DNS_SD, this.registrationListener);
        }

    }

    public void unregister() {
        if (registred) {
            this.manager.unregisterService(this.registrationListener);
            this.registrationListener = null;
        }
        try {
            this.manager.stopServiceDiscovery(this.discoveryListener);
        } catch (IllegalArgumentException e) {
            Log.e(TAG, "", e);
        }
        this.serviceListener.stop();
    }

    public void startDiscovery() {
        this.manager.discoverServices(SERVICE_TYPE, NsdManager.PROTOCOL_DNS_SD, this.discoveryListener);
        this.serviceListener.startAsServer();
    }


    public interface ServiceListener {
        void connectTo(InetAddress address, int port);

        InetSocketAddress startAsServer();

        void stop();
    }

    public interface DiscoveryListener {
        void found();
    }
}

