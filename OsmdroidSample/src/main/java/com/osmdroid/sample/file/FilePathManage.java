package com.osmdroid.sample.file;

import android.os.Environment;

import java.io.File;

public class FilePathManage {
	private volatile static FilePathManage mDebugFile = null;

	public static FilePathManage GetInstance() {
		if (mDebugFile == null) {
			synchronized (FilePathManage.class){
				if (mDebugFile == null){
					mDebugFile = new FilePathManage();
					mDebugFile.init();
				}
			}
		}
		return mDebugFile;
	}

	private void init() {
		mDebugFile.mstrFilePathString = Environment.getExternalStorageDirectory().getPath() +
				"/com_map";
		File projFile = new File(mDebugFile.mstrFilePathString);
		if (!projFile.exists()) {
			projFile.mkdir();
		}
		projFile = new File(mDebugFile.mstrFilePathString+"/map");
		if (!projFile.exists()) {
			projFile.mkdir();
		}
		projFile = new File(mDebugFile.mstrFilePathString+"/data");
		if (!projFile.exists()) {
			projFile.mkdir();
		}
	}

	String mstrFilePathString = "";
	public String getFilePathString() {
		return mstrFilePathString;
	}
	
	public String getMapDirectory() {
		return mDebugFile.mstrFilePathString + "/map";
	}
	
	public String getDataDirectory() {
		return mDebugFile.mstrFilePathString + "/data";
	}
	
}
