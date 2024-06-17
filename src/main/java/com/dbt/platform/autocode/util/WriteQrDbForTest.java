package com.dbt.platform.autocode.util;

import java.io.FileOutputStream;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CountDownLatch;

/**
 * 仅供测试使用
 * @author jiquanwei
 *
 */
public class WriteQrDbForTest implements Runnable
{
  private ConcurrentLinkedQueue<String> queDb;
  private FileOutputStream file;
  private CountDownLatch latch;

  public WriteQrDbForTest(ConcurrentLinkedQueue<String> que, FileOutputStream fi, CountDownLatch latch)
  {
    this.queDb = que;
    this.file = fi;
    this.latch = latch;
  }

  public void run()
  {
	  try
      {
		while (!(this.queDb.isEmpty())) {
	        String separator = System.getProperty("line.separator");
	        file.write((queDb.poll() + separator).getBytes());
		}
		file.flush();
	    latch.countDown();
      } catch (Exception e) {
          e.printStackTrace();
     }
  }
}