package org.dasd.stemcell;

@Deprecated
public class ProcessManager {

	private static boolean libraryLoaded = false;
	private static int[] originalFrontProcess = null;
	private static boolean inFront = false;

	static {
		try {
			System.loadLibrary("carbon-process-manager"); //$NON-NLS-1$
			libraryLoaded = true;
		} catch (Exception e) {
			// fail gracefully as a precaution if other platforms accidentally load this class
		}
	}

	public static final native int GetCurrentProcess(int[] psn);

	public static final native int GetFrontProcess(int[] psn);

	public static final native int SetFrontProcess(int[] psn);

	/**
	 * Bring the current process to the front.  This method will save the
	 * current front process, that will be restored with a call to processToBack();
	 * <p>
	 * This function must be followed with a call to processToBack() before being called again.
	 */
	public static void processToFront() {
		if (libraryLoaded && !inFront) {
			// Get the process serial number of the current front process
			// This should be Eclipse's process id
			originalFrontProcess = new int[2];
			GetFrontProcess(originalFrontProcess);

			// Get the process serial number for the current process
			// This should be the java remote vm's process id
			int[] currentProcess = new int[2];
			GetCurrentProcess(currentProcess);
			if (currentProcess[1] != 0) {
				// Set the java remote vm to be the front process
				SetFrontProcess(currentProcess);
				inFront = true;
			}
		}
	}

	/**
	 * Restore the old front process to the front, sending the current process to the back.
	 * <p>
	 * This function must follow a call to processToFront()
	 */
	public static void processToBack() {
		if (libraryLoaded && inFront) {
			if (originalFrontProcess != null && originalFrontProcess[1] != 0) {
				// Restore the original front process
				SetFrontProcess(originalFrontProcess);
				originalFrontProcess = null;
			}
			inFront = false;
		}
	}
}




