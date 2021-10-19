package com.org.appcrashtracker;

import ohos.aafwk.ability.Ability;
import ohos.aafwk.content.Intent;
import ohos.aafwk.content.Operation;
import ohos.app.Context;
import ohos.batterymanager.BatteryInfo;
import ohos.bundle.BundleInfo;
import ohos.bundle.IBundleManager;
import ohos.data.usage.MountState;
import ohos.data.usage.StatVfs;
import ohos.global.configuration.Configuration;
import ohos.global.configuration.DeviceCapability;
import ohos.global.resource.NotExistException;
import ohos.global.resource.WrongTypeException;
import ohos.hiviewdfx.Debug;
import ohos.hiviewdfx.HiChecker;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;
import ohos.net.NetCapabilities;
import ohos.net.NetManager;
import ohos.os.ProcessManager;
import ohos.rpc.RemoteException;
import ohos.security.SystemPermission;
import ohos.system.DeviceInfo;
import ohos.telephony.RadioInfoManager;
import ohos.telephony.SignalInformation;
import ohos.telephony.SimInfoManager;
import ohos.telephony.TelephonyConstants;
import ohos.utils.zson.ZSONException;
import ohos.utils.zson.ZSONObject;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static com.example.appcrashtracker.ResourceTable.*;


public class ExceptionHandler implements
		Thread.UncaughtExceptionHandler {
	private final Ability ability;
	Intent intent ;
	ZSONObject jObjectData;
	String AbilityName;
	Class<?> name;
	String Post_Url;

	private boolean class_name = false;
	private boolean message = false;
	private boolean localized_message = false;
	private boolean causes = false;
	private boolean stack_trace = false;
	private boolean brand_name = false;
	private boolean device_name = false;
	private boolean sdk_version = false;
	private boolean release = false;
	private boolean height = false;
	private boolean width = false;
	private boolean app_version = false;
	private boolean tablet = false;
	private boolean device_orientation = false;
	private boolean screen_layout = false;
	private boolean vm_heap_size = false;
	private boolean allocated_vm_size = false;
	private boolean vm_max_heap_size = false;
	private boolean vm_free_heap_size = false;
	private boolean native_allocated_size = false;
	private boolean battery_percentage = false;
	private boolean battery_charging = false;
	private boolean battery_charging_via= false;
	private boolean sd_card_status= false;
	private boolean internal_memory_size= false;
	private boolean external_memory_size= false;
	private boolean internal_free_space= false;
	private boolean external_free_space= false;
	private boolean package_name= false;
	private boolean network_mode= false;
	private boolean country= false;

	public ExceptionHandler(Ability ability,Class<?> name)throws NotExistException, WrongTypeException, IOException {
		this.ability = ability;
		this.name=name;
		AbilityName=ability.getClass().getSimpleName();

		this.Post_Url=ability.getString(String_url);
		if(Post_Url.equals("default_url"))
			HiLog.error(new HiLogLabel(HiLog.LOG_APP, 0x00201 ,""+ability.getBundleName()), "Post url not specified");
		else
		{
			class_name = ability.getResourceManager().getElement(Boolean_class_name).getBoolean();
			message = ability.getResourceManager().getElement(Boolean_message).getBoolean();
			localized_message = ability.getResourceManager().getElement(Boolean_localized_message).getBoolean();
			causes = ability.getResourceManager().getElement(Boolean_causes).getBoolean();
			stack_trace = ability.getResourceManager().getElement(Boolean_stack_trace).getBoolean();
			brand_name = ability.getResourceManager().getElement(Boolean_brand_name).getBoolean();
			device_name = ability.getResourceManager().getElement(Boolean_device_name).getBoolean();
			sdk_version = ability.getResourceManager().getElement(Boolean_sdk_version).getBoolean();
			release = ability.getResourceManager().getElement(Boolean_release).getBoolean();
			height = ability.getResourceManager().getElement(Boolean_height).getBoolean();
			width = ability.getResourceManager().getElement(Boolean_width).getBoolean();
			app_version = ability.getResourceManager().getElement(Boolean_app_version).getBoolean();
			tablet = ability.getResourceManager().getElement(Boolean_tablet).getBoolean();
			device_orientation=ability.getResourceManager().getElement(Boolean_device_orientation).getBoolean();
			screen_layout=ability.getResourceManager().getElement(Boolean_screen_layout).getBoolean();
			vm_heap_size = ability.getResourceManager().getElement(Boolean_vm_heap_size).getBoolean();
			allocated_vm_size = ability.getResourceManager().getElement(Boolean_allocated_vm_size).getBoolean();
			vm_max_heap_size = ability.getResourceManager().getElement(Boolean_vm_max_heap_size).getBoolean();
			vm_free_heap_size = ability.getResourceManager().getElement(Boolean_vm_free_heap_size).getBoolean();
			native_allocated_size = ability.getResourceManager().getElement(Boolean_native_allocated_size).getBoolean();
			battery_percentage = ability.getResourceManager().getElement(Boolean_battery_percentage).getBoolean();
			battery_charging = ability.getResourceManager().getElement(Boolean_battery_charging).getBoolean();
			battery_charging_via = ability.getResourceManager().getElement(Boolean_battery_charging_via).getBoolean();
			sd_card_status = ability.getResourceManager().getElement(Boolean_sd_card_status).getBoolean();
			internal_memory_size = ability.getResourceManager().getElement(Boolean_internal_memory_size).getBoolean();
			external_memory_size = ability.getResourceManager().getElement(Boolean_external_memory_size).getBoolean();
			internal_free_space = ability.getResourceManager().getElement(Boolean_internal_free_space).getBoolean();
			external_free_space = ability.getResourceManager().getElement(Boolean_external_free_space).getBoolean();
			package_name = ability.getResourceManager().getElement(Boolean_package_name).getBoolean();
			network_mode = ability.getResourceManager().getElement(Boolean_network_mode).getBoolean();
			country = ability.getResourceManager().getElement(Boolean_country).getBoolean();
		}
	}

	public void uncaughtException(Thread thread, Throwable exception)  {
		StringWriter stackTrace = new StringWriter();
		exception.printStackTrace(new PrintWriter(stackTrace));

        HiChecker.removeAllRules();

		jObjectData = new ZSONObject();
		try {
			BundleInfo bi = new BundleInfo();
			if(package_name)
				jObjectData.put("Package_Name", ability.getBundleName());
			if(class_name)
				jObjectData.put("Class", AbilityName);
			if(message)
				jObjectData.put("Message", exception.getMessage());
			if(localized_message)
				jObjectData.put("Localized_Message", exception.getLocalizedMessage());
			if(causes)
				jObjectData.put("Cause", exception.getCause());
			if(stack_trace)
				jObjectData.put("Stack_Trace", stackTrace.toString());
			if(brand_name)
				jObjectData.put("Brand", "Huawei");
			if(device_name)
				jObjectData.put("Device", DeviceInfo.getName());
			if(sdk_version)
				jObjectData.put("SDK",bi.getMaxSdkVersion());
			if(release)
				jObjectData.put("Release",bi.getVersionName());
			if(height)
				jObjectData.put("Height", ability.getResourceManager().getDeviceCapability().height );
			if(width)
				jObjectData.put("Width", ability.getResourceManager().getDeviceCapability().width);
			if(app_version)
				jObjectData.put("App_Version", getAppVersion(ability));
			if(tablet)
				jObjectData.put("Tablet", isTablet(ability));
			if(device_orientation)
				jObjectData.put("Device_Orientation", getScreenOrientation(ability));
			if(screen_layout)
				jObjectData.put("Screen_Layout", getScreenLayout(ability));
			if(vm_heap_size)
				jObjectData.put("VM_Heap_Size", ConvertSize(Runtime.getRuntime().totalMemory()));
			if(allocated_vm_size)
				jObjectData.put("Allocated_VM_Size", ConvertSize(Runtime.getRuntime().freeMemory()));
			if(vm_max_heap_size)
				jObjectData.put("VM_Max_Heap_Size", ConvertSize((Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory())));
			if(vm_free_heap_size)
				jObjectData.put("VM_free_Heap_Size", ConvertSize(Runtime.getRuntime().maxMemory()));
			if(native_allocated_size)
				jObjectData.put("Native_Allocated_Size", HeapSize(ability));
			if(battery_percentage)
				jObjectData.put("Battery_Percentage", getBatteryChargeLevel());
			if(battery_charging)
				jObjectData.put("Battery_Charging_Status", getBatteryStatus(ability));
			if(battery_charging_via)
				jObjectData.put("Battery_Charging_Via", getBatteryChargingMode(ability));
			if(sd_card_status)
				jObjectData.put("SDCard_Status", getSDCardStatus(ability));
			if(internal_memory_size)
				jObjectData.put("Internal_Memory_Size",  getTotalInternalMemorySize(ability));
			if(external_memory_size)
				jObjectData.put("External_Memory_Size", getTotalExternalMemorySize(ability));
			if(internal_free_space)
				jObjectData.put("Internal_Free_Space",  getAvailableInternalMemorySize(ability));
			if(external_free_space)
				jObjectData.put("External_Free_Space",  getAvailableExternalMemorySize(ability));
			if(network_mode)
				jObjectData.put("Network_Mode", getNetworkMode(ability));
			if(country)
				jObjectData.put("Country", new Locale("",ability.getResourceManager().getConfiguration().getFirstLocale().getCountry()).getDisplayCountry());
		} catch (ZSONException e) {
			HiLog.error(new HiLogLabel(HiLog.LOG_APP, 0x00201 ,""+ability.getBundleName()), "JSON Exception");
		}
		HiLog.info(new HiLogLabel(HiLog.LOG_APP, 0x00201 ,""), ">>>>>>>>>>>>>>>>>>"+getNetworkOperatorName(ability));


		if(ability.getBundleManager().checkPermission( ability.getBundleName() , SystemPermission.INTERNET) == 0)
		{
			if((ability.getBundleManager().checkPermission(ability.getBundleName() , SystemPermission.GET_NETWORK_INFO) ==0 ))
			{
				if(isConnectingToInternet(ability))
				{

					if (class_name || message || localized_message || causes
							|| stack_trace || brand_name || device_name
							|| sdk_version || release  || height || width
							|| app_version || tablet || device_orientation
							|| screen_layout || vm_heap_size
							|| allocated_vm_size || vm_max_heap_size
							|| vm_free_heap_size || native_allocated_size
							|| battery_percentage || battery_charging
							|| battery_charging_via || sd_card_status
							|| internal_memory_size || external_memory_size
							|| internal_free_space || external_free_space
							|| package_name || network_mode || country) {
						ability.getUITaskDispatcher().asyncDispatch(() -> {
							try{
								URL url = null;
								try {
									url = new URL(Post_Url);
								} catch (MalformedURLException e1) {
									HiLog.error(new HiLogLabel(HiLog.LOG_APP, 0x00201 ,""+ability.getBundleName()), "MalformedURLException");
								}
								HttpURLConnection conn = null;
								try {
									conn = (HttpURLConnection) url.openConnection();
								} catch (IOException e1) {
									HiLog.error(new HiLogLabel(HiLog.LOG_APP, 0x00201 ,""+ability.getBundleName()), "IOException");
								}
								try {
									conn.setRequestMethod("POST");
								} catch (ProtocolException e1) {
									HiLog.error(new HiLogLabel(HiLog.LOG_APP, 0x00201 ,""+ability.getBundleName()), "ProtocolException");
								}
								conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
								conn.setDoInput(true);
								conn.setDoOutput(true);
								List<PostValuesPair> params1 = new ArrayList<>();
								params1.add(new PostValuesPair("error_report", jObjectData.toString()));
								try{
									OutputStream os = conn.getOutputStream();
									BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, StandardCharsets.UTF_8));
									writer.write(getQuery(params1));
									writer.flush();
									writer.close();
									os.close();
								}
								catch(Exception ee)
								{
									HiLog.error(new HiLogLabel(HiLog.LOG_APP, 0x00201 ,""+ability.getBundleName()), "Buffer Write Exception");
								}
								try {
									conn.connect();
								} catch (IOException e1) {
									HiLog.error(new HiLogLabel(HiLog.LOG_APP, 0x00201 ,""+ability.getBundleName()), "IOException");
								}
							} catch (Exception e) {
								HiLog.error(new HiLogLabel(HiLog.LOG_APP, 0x00201 ,""+ability.getBundleName()), "Exception Occurred");
							}
						});
					}
					else
						HiLog.error(new HiLogLabel(HiLog.LOG_APP, 0x00201 ,""+ability.getBundleName()), "Not configured. Set configuration in string.json");
				}
				else
					HiLog.error(new HiLogLabel(HiLog.LOG_APP, 0x00201 ,""+ability.getBundleName()), "Network not found");
			}
			else
				HiLog.error(new HiLogLabel(HiLog.LOG_APP, 0x00201 ,""+ability.getBundleName()), "Need to add Access network state permission");
		}
		else
			HiLog.error(new HiLogLabel(HiLog.LOG_APP, 0x00201 ,""+ability.getBundleName()), "Need to add internet permission");

		intent = new Intent();
		Operation operation = new Intent.OperationBuilder()
				.withBundleName(ability.getBundleName())
				.withAbilityName(ability.getAbilityName())
				.build();
		intent.setOperation(operation);
		ability.startAbility(intent);
		ProcessManager.kill(ProcessManager.getPid());
		System.exit(10);

	}

	private String getNetworkOperatorName(Context con) {
		String operator = "";
		SimInfoManager sm = SimInfoManager.getInstance(con);
		int max_sim = sm.getMaxSimCount();
		for(int i=0; i<max_sim; i++) {
			if (sm.hasSimCard(i)) {
				RadioInfoManager radioInfoManager = RadioInfoManager.getInstance(con);
				operator+= " "+"Sim "+(i+1)+": "+radioInfoManager.getOperatorName(i);
			}
		}
		return operator;
	}

	private String getBatteryChargeLevel() {
		BatteryInfo bi = new BatteryInfo();
		BatteryInfo.BatteryLevel level = bi.getBatteryLevel();
		switch (level){
			case LOW:
				return "LOW";
			case NORMAL:
				return "NORMAL";
			case HIGH:
				return "HIGH";
			case RESERVED:
				return "RESERVED";
			case EMERGENCY:
				return "EMERGENCY";
			default:
				return "Unknowm";
		}
	}

	private String HeapSize(Ability abl) {
		Debug.HeapInfo heapInfo = new Debug.HeapInfo();
		Debug.getNativeHeapInfo(heapInfo);
		long heapSize = heapInfo.nativeHeapSize;
		return ConvertSize(heapSize);
	}

	public String getAppVersion(Context con)
	{
		IBundleManager manager = con.getBundleManager();
		BundleInfo info = null;
		try {
			info = manager.getBundleInfo(con.getBundleName(), 0);
		} catch ( RemoteException e) {
			HiLog.error(new HiLogLabel(HiLog.LOG_APP, 0x00201 ,""+ability.getBundleName()), "Name not found Exception");
		}
		return info.getVersionName();
	}

	public boolean isTablet(Context con) {
		int tablet = con.getResourceManager().getDeviceCapability().deviceType;
		return tablet == DeviceCapability.DEVICE_TYPE_TABLET;

	}

	public String getQuery(List<PostValuesPair> params) throws UnsupportedEncodingException
	{
		StringBuilder result = new StringBuilder();
		boolean first = true;

		for (PostValuesPair pair : params)
		{
			if (first)
				first = false;
			else
				result.append("&");

			result.append(URLEncoder.encode(pair.getKey(), "UTF-8"));
			result.append("=");
			result.append(URLEncoder.encode(pair.getValue(), "UTF-8"));
		}

		return result.toString();
	}

	@SuppressWarnings("deprecation")
	public boolean isConnectingToInternet(Context con){
		NetManager netManager = NetManager.getInstance(con);
		NetCapabilities netCapabilities = netManager.getNetCapabilities(netManager.getDefaultNet());
		return netCapabilities.hasCap(NetCapabilities.NET_CAPABILITY_VALIDATED);
	}

	@SuppressWarnings("deprecation")
	public String getScreenOrientation(Ability abl)
	{

		int screenOrientation =  abl.getDisplayOrientation();
		switch (screenOrientation){
			case (Configuration.DIRECTION_HORIZONTAL):
				return "LandScape";
			case (Configuration.DIRECTION_VERTICAL):
				return "Portrait";
			default:
				return "Not Defined";
		}

	}
	public String getScreenLayout(Ability abl)
	{
		int screenSize = abl.getResourceManager().getDeviceCapability().screenDensity;
		switch (screenSize) {
			case DeviceCapability.SCREEN_LDPI:
				return "Large Screen";
			case DeviceCapability.SCREEN_MDPI:
				return  "Mediun Screen";
			case DeviceCapability.SCREEN_SDPI:
				return  "Small Screen";
			case DeviceCapability.SCREEN_XLDPI:
				return "Extra Large Screen";
			case DeviceCapability.SCREEN_XXLDPI:
				return "Extra Extra Large Screen";
			case DeviceCapability.SCREEN_XXXLDPI:
				return "Extra Extra Extra Large Screen";
			default:
				return "Screen size is not defined";
		}
	}

	public String ConvertSize(long size) {
		if(size <= 0) return "0";
		final String[] units = new String[] { "B", "kB", "MB", "GB", "TB" };
		int digitGroups = (int) (Math.log10(size)/Math.log10(1024));
		return new DecimalFormat("#,##0.#").format(size/Math.pow(1024, digitGroups)) + " " + units[digitGroups];
	}

	public String getBatteryStatus(Ability abl)
	{
		BatteryInfo bi = new BatteryInfo();
		BatteryInfo.BatteryChargeState status = bi.getChargingStatus();
		switch (status){
			case DISABLE:
				return "Not Charging";
			case ENABLE:
				return "Charging";
			case FULL:
				return "Battery Full";
			case RESERVED:
				return"Reseved";
			default:
				return"Unknown";
		}
	}

	public String getBatteryChargingMode(Ability abl)
	{
		BatteryInfo bi = new BatteryInfo();
		BatteryInfo.BatteryPluggedType type = bi.getPluggedType();
		switch (type){
			case AC:
				return "AC";
			case USB:
				return "USB";
			case WIRELESS:
				return "WIRELESS";
			default:
				return "Unknown";

		}
	}

	public String getSDCardStatus(Ability abl)
	{
		boolean storage = ohos.data.usage.DataUsage.isSupported();
		if (storage){
			return "External Storage Not Suported";
		}
		else{
			MountState isSDPresent = ohos.data.usage.DataUsage.getDiskMountedStatus();
			switch (isSDPresent) {
				case DISK_MOUNTED:
					return "Mounted";
				case DISK_REMOVED:
					return "Removed";
				case DISK_UNMOUNTED:
					return "Unmounted";
				default:
					return "Unknown";
			}
		}
	}

	@SuppressWarnings("deprecation")
	public String getAvailableInternalMemorySize(Context con ) {
		FilePath fp  = new FilePath();
		File path = FilePath.getInternalStorage(con);
		StatVfs stat = new StatVfs(path.getPath());
		long availableBlocks = stat.getFreeSpace();
		return ConvertSize(availableBlocks);
	}

	@SuppressWarnings("deprecation")
	public String getTotalInternalMemorySize(Context con) {
		FilePath fp  = new FilePath();
		File path = FilePath.getInternalStorage(con);
		StatVfs stat = new StatVfs(path.getPath());
		long totalBlocks = stat.getSpace();
		return ConvertSize(totalBlocks);
	}

	@SuppressWarnings("deprecation")
	public String getAvailableExternalMemorySize(Context con) {
		if (ohos.data.usage.DataUsage.getDiskMountedStatus().equals(MountState.DISK_MOUNTED)) {
			FilePath fp  = new FilePath();
			File path = FilePath.getExternalStorage(con);
			StatVfs stat = new StatVfs(path.getPath());
			long availableBlocks = stat.getFreeSpace();
			return ConvertSize(availableBlocks);
		} else {
			return "SDCard not present";
		}
	}

	@SuppressWarnings("deprecation")
	public String getTotalExternalMemorySize(Context con) {
		if (ohos.data.usage.DataUsage.getDiskMountedStatus().equals(MountState.DISK_MOUNTED)) {
			FilePath fp  = new FilePath();
			File path = FilePath.getExternalStorage(con);
			StatVfs stat = new StatVfs(path.getPath());
			long totalBlocks = stat.getSpace();
			return ConvertSize(totalBlocks);
		} else {
			return "SDCard not present";
		}
	}
	@SuppressWarnings("deprecation")
	public String getNetworkMode(Context con) {
		NetManager netManager = NetManager.getInstance(con);
		NetCapabilities netCapabilities = netManager.getNetCapabilities(netManager.getDefaultNet());
		if (netCapabilities.hasCap(NetCapabilities.NET_CAPABILITY_VALIDATED) &&
				netCapabilities.hasBearer(NetCapabilities.BEARER_WIFI) ||
				netCapabilities.hasBearer(NetCapabilities.BEARER_WIFI_AWARE)) {
			return "Wifi";
		}
		else if (netCapabilities.hasCap(NetCapabilities.NET_CAPABILITY_VALIDATED) &&
				netCapabilities.hasBearer(NetCapabilities.BEARER_CELLULAR)) {
			String networkMode = "";
			SimInfoManager sm = SimInfoManager.getInstance(con);
			int max_sim = sm.getMaxSimCount();
			for(int i=0; i<max_sim; i++){
				if(sm.hasSimCard(i)){
					RadioInfoManager radioInfoManager = RadioInfoManager.getInstance(con);
					List<SignalInformation> signalList = radioInfoManager.getSignalInfoList(i);
					for(SignalInformation signal : signalList){
						int signalNetworkType = signal.getNetworkType();
						if (signalNetworkType == TelephonyConstants.NETWORK_TYPE_CDMA){
							networkMode+= " "+"Sim "+(i+1)+": CDMA";
						}
						if (signalNetworkType == TelephonyConstants.NETWORK_TYPE_GSM){
							networkMode+= " "+"Sim "+(i+1)+": GSM";
						}
						if (signalNetworkType == TelephonyConstants.NETWORK_TYPE_UNKNOWN){
							networkMode+= " "+"Sim "+(i+1)+": UNKNOWN";
						}
						if (signalNetworkType == TelephonyConstants.NETWORK_TYPE_WCDMA){
							networkMode+= " "+"Sim "+(i+1)+": WCDMA";
						}
						if (signalNetworkType == TelephonyConstants.NETWORK_TYPE_TDSCDMA){
							networkMode+= " "+"Sim "+(i+1)+": TDSCDMA";
						}
						if (signalNetworkType == TelephonyConstants.NETWORK_TYPE_LTE){
							networkMode+= " "+"Sim "+(i+1)+": LTE";
						}
						if (signalNetworkType == TelephonyConstants.NETWORK_TYPE_NR){
							networkMode+= " "+"Sim "+(i+1)+": NR";
						}
					}

				}
			}
			return networkMode;
		}
		else
		{
			return "No Network";
		}
	}
}
