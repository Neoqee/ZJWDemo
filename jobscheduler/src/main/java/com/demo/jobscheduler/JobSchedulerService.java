package com.demo.jobscheduler;

import android.app.job.JobParameters;
import android.app.job.JobService;

import com.demo.demolib.ApiCallback;
import com.demo.demolib.controller.ApiController;

public class JobSchedulerService extends JobService {
    @Override
    public boolean onStartJob(final JobParameters params) {
        System.out.println("startjob------zjw");
        if(params.getJobId()==1){
            System.out.println("startjob for id is 1");
            ApiController.getBannerList(new ApiCallback() {
                @Override
                public void onSuccess(String string) {
                    System.out.println(string);
                    jobFinished(params,false);
                }

                @Override
                public void onFailure(Throwable throwable) {
                    jobFinished(params,false);
                    throwable.printStackTrace();
                }
            });
        }
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        System.out.println("stop-----zjw");
        return false;
    }
}
