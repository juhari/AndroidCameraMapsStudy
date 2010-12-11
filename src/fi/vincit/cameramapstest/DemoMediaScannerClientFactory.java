package fi.vincit.cameramapstest;

import android.content.Context;

public class DemoMediaScannerClientFactory {
	private static DemoMediaScannerClient connection = null;
	
	public static DemoMediaScannerClient createInstance(Context ctx)
	{
		if( connection == null )
		{
			connection = new DemoMediaScannerClient(ctx);
		}
		return connection;
	}
	
	public static DemoMediaScannerClient getInstance() {
		return connection;
	}
	
}
