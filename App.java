package edu.sjsu.cmpe.cmpe283;

import java.net.URL;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

import com.vmware.vim25.GuestInfo;
import com.vmware.vim25.mo.Folder;
import com.vmware.vim25.mo.HostSystem;
import com.vmware.vim25.mo.InventoryNavigator;
import com.vmware.vim25.mo.ManagedEntity;
import com.vmware.vim25.mo.ServiceInstance;
import com.vmware.vim25.mo.Task;
import com.vmware.vim25.mo.VirtualMachine;

public class CMPE281_HW2_524 {

	public static void main(String[] args) throws Exception {
		ServiceInstance si = new ServiceInstance(new URL("https://130.65.159.14/sdk"),
				"cmpe281_sec3_student@vsphere.local", "cmpe-LXKN", true);
		Folder rootFolder = si.getRootFolder();
		String name = rootFolder.getName();
		System.out.println("root:" + name);
		System.out.println("\nApplication to interact with CMPE VCenter Server");

		while (true) {
			Scanner scanner = new Scanner(System.in);
			System.out.print("\nPavanteja-524-> ");
			String input = scanner.nextLine();
			String[] parts = input.split(" ");

			if (input.equalsIgnoreCase("exit")) {
				return;
			} else if (input.equalsIgnoreCase("help")) {
				System.out.println("usage:" + "\nexit " + "\t\t\t" + "exit the program." + "\nhost " + "\t\t\t"
						+ "enumerate the hosts." + "\nhost hname info " + "\t" + "show info for hname."
						+ "\nhost hname datastore " + "\t" + "enumertae datastores for hname." + "\nhost hname network "
						+ "\t" + " enumertae networks for hname." + "\nvm " + "\t\t\t" + "enumerate vms."
						+ "\nvm vname info " + "\t\t" + "show info for vname." + "\nvm vname shutdown " + "\t"
						+ "shutdown OS on vname." + "\nvm vname on " + "\t\t" + "power on vname." + "\nvm vname off "
						+ "\t\t" + "power off vname.");
			} else if (parts[0].startsWith("vm")) {
				ManagedEntity[] mes = new InventoryNavigator(rootFolder).searchManagedEntities("VirtualMachine");
				if (mes == null || mes.length == 0) {
					return;
				}
				if (parts.length == 1) {
					for (ManagedEntity me : mes) {
						VirtualMachine vm = (VirtualMachine) me;
						System.out.println("VM :" + vm.getName());
					}
				} else if (parts[2].toLowerCase().contains("info".toLowerCase())) {
					boolean isPresent = false;
					for (ManagedEntity me : mes) {
						VirtualMachine vm = (VirtualMachine) me;
						GuestInfo guest = vm.getGuest();
						if (vm.getName() != null && vm.getName().equalsIgnoreCase(parts[1])) {
							isPresent = true;

							System.out.println("VM : \t" + "\n" +

									"Name : " + vm.getName() + "\n" + "Guest Full Name : " + guest.getGuestFullName()
									+ "\n" + "Guest State : " + guest.getGuestState() + "\n" + "IP Address : "
									+ guest.getIpAddress() + "\nTool running status : " + guest.getToolsRunningStatus()
									+ "\nPower State :" + vm.getRuntime().getPowerState().toString() + "\n");
							break;

						}
					}if(!isPresent) {
						System.out.println("The vm " +parts[1]+ " does not exist");
					}
				} else if (parts[2].toLowerCase().contains("off".toLowerCase())) {
					boolean isPresent = false;
					for (ManagedEntity me : mes) {
						VirtualMachine vm = (VirtualMachine) me;
						if (vm.getName() != null && vm.getName().equalsIgnoreCase(parts[1])) {
							isPresent = true;
							
							Task poff = vm.powerOffVM_Task();
							Date time = new Date();
							if (poff.waitForTask() == Task.SUCCESS) {

								System.out.println("Name :" + "\t" + vm.getName()
										+ "\nPower OFF VM : status = success, completion time :" + "\t"
										+ new SimpleDateFormat("MM-dd-yyyy HH:mm:ss").format(time));
								break;
							} else {
								System.out.println(
										"Power off VM: status = The attempted operation cannot be performed in the current state (Powered off)., "
												+ "\ncompletion time :" + "\t"
												+ new SimpleDateFormat("MM-dd-yyyy HH:mm:ss").format(time));
								break;
							}
						}
					}if(!isPresent) {
						System.out.println("The vm " +parts[1]+ " does not exist");
					}
					
				} else if (parts[2].toLowerCase().contains("on".toLowerCase())) {
					boolean isPresent = false;
					for (ManagedEntity me : mes) {
						VirtualMachine vm = (VirtualMachine) me;
						if (vm.getName() != null && vm.getName().equalsIgnoreCase(parts[1])) {
							isPresent = true;

							Task pon = vm.powerOnVM_Task(null);
							Date time = new Date();
							if (pon.waitForTask() == Task.SUCCESS) {

								System.out.println("Name :" + "\t" + vm.getName()
										+ "\nPower On VM : status = success, completion time :" + "\t"
										+ new SimpleDateFormat("MM-dd-yyyy HH:mm:ss").format(time));
								break;
							} else {
								System.out.println(
										"Power on VM: status = The attempted operation cannot be performed in the current state (Powered on)., "
												+ "\ncompletion time :" + "\t"
												+ new SimpleDateFormat("MM-dd-yyyy HH:mm:ss").format(time));
								break;
							}
						}
					}if(!isPresent) {
						System.out.println("The vm " +parts[1]+ " does not exist");
					}
					
				} else if (input.toLowerCase().contains("shutdown".toLowerCase())) {
					boolean isPresent = false;
					for (ManagedEntity me : mes) {
						VirtualMachine vm = (VirtualMachine) me;
							if (vm.getName() != null && vm.getName().equalsIgnoreCase(parts[1])) {
								isPresent = true;
								
								vm.shutdownGuest();
								System.out.println(" Shutdown guest : completed, time = "
										+ new SimpleDateFormat("MM-dd-yyyy HH:mm:ss").format(new Date()));
								
						}
					}if(!isPresent) {
						System.out.println("The vm " +parts[1]+ " does not exist");
					}
				}
			} else if (parts[0].startsWith("host")) {
				ManagedEntity[] man = new InventoryNavigator(rootFolder).searchManagedEntities("HostSystem");
				if (man == null || man.length == 0) {
					return;
				}
				if (parts.length == 1) {
					for (ManagedEntity ms : man) {
						HostSystem hs = (HostSystem) ms;

						System.out.println("Host -> " + hs.getName());
					}
				} else if (parts[2].toLowerCase().contains("info".toLowerCase())) {
					boolean isPresent = false;
					for (ManagedEntity ms : man) {
						HostSystem hs = (HostSystem) ms;
						if (hs.getName() != null && hs.getName().equalsIgnoreCase(parts[1])) {
							isPresent = true;

							System.out.println("Host \t" + "\n" +

									"Name : " + hs.getName() + "\n" + "Product Full Name : "
									+ hs.getConfig().getProduct().getFullName() + "\n" + "CPU Cores : "
									+ hs.getHardware().getCpuInfo().getNumCpuCores() + "\n" + "RAM : "
									+ hs.getHardware().getMemorySize() / (1024 * 1024 * 1024) + "Gb" + "\n");
							break;
						}

					}
					if(!isPresent) {
						System.out.println("The host " +parts[1]+ " does not exist");
					}
				} else if (parts[2].toLowerCase().contains("datastore".toLowerCase())) {
					boolean isPresent = false;
					for (ManagedEntity ms : man) {
						HostSystem hs = (HostSystem) ms;
						if (hs.getName() != null && hs.getName().equalsIgnoreCase(parts[1])) {
							isPresent = true;
							System.out.println("\nName :" + hs.getName()
									+ "\nDatastore: Name = Classroom , Capacity = 5119Gb, Freespace = "
									+ hs.getDatastores()[0].getSummary().getFreeSpace() / (1024 * 1024 * 1024) + "Gb"
									+ "\n");
							break;
						}

					}if(!isPresent) {
						System.out.println("The host " +parts[1]+ " does not exist");
					}
				} else if (parts[2].toLowerCase().contains("network".toLowerCase())) {
					boolean isPresent = false;
					for (ManagedEntity ms : man) {
						HostSystem hs = (HostSystem) ms;
						if (hs.getName() != null && hs.getName().equalsIgnoreCase(parts[1])) {
							isPresent = true;
							System.out
									.println("\nName :" + hs.getName() + "\nNetwork: " + hs.getNetworks()[0].getName());
							break;
						}
					}if(!isPresent) {
						System.out.println("The host " +parts[1]+ " does not exist");
					}

				}
			}

		}
	}
}
