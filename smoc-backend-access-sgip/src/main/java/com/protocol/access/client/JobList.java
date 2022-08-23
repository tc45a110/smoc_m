package com.protocol.access.client;

import org.apache.log4j.Logger;

import com.base.common.manager.ResourceManager;
import com.protocol.access.sgip.pdu.Request;
import com.protocol.access.sgip.pdu.Terminate;
import com.protocol.access.util.SequenceUtil;


public class JobList {
	private static Logger logger = Logger.getLogger(JobList.class);
    private java.util.List<Request> objs = new java.util.Vector<Request>();//发�?�队�?

    private int maxobjs;

	public JobList(int maxobjs) {
        this.maxobjs = maxobjs;
    }
    
    public  Request getJob() {

        long timeMillis = System.currentTimeMillis();
        //及时调用了stopGetJob,也要等objs里面发�?�完�?
        while (true) {
            try {
				synchronized (this) {
					if (objs.size() > 0) {
						Request submit = null;
						try {
							submit = objs.remove(0);
						} catch (Exception e) {
							logger.error(e.getMessage());
						}
						return submit;
					}
				}
				int activeTimer = ResourceManager.getInstance().getIntValue("sgip.client.active.interval");
				if((System.currentTimeMillis() - timeMillis) > activeTimer){
            		logger.info("超过"+activeTimer+"毫秒,无发送数据！");
            		Terminate terminate = new Terminate();
            		terminate.setSequenceId(SequenceUtil.getSequence());
            		return terminate;
            	}
				try {
					Thread.sleep(10);
				} catch (Exception e) {
					logger.error(e.getMessage(),e);
				} 
				
			} catch (Exception e) {
				logger.error(e.getMessage(),e);
			}

        }
    }

    public boolean addJob(Request submit) {
        if (objs.size() >= maxobjs) {
            return false;
        }
        objs.add(submit);
        return true;
    }

    public int getSize() {

        return objs.size();
    }

}