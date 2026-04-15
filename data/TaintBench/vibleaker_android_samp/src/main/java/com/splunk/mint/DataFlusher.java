package com.splunk.mint;

import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

class DataFlusher extends BaseExecutor implements InterfaceExecutor {
    private static final int MAX_FILE_SIZE = 3000000;

    DataFlusher() {
    }

    public synchronized void send() {
        Thread t = new LowPriorityThreadFactory().newThread(new Runnable() {
            public void run() {
                if (!Utils.allowedToSendData()) {
                    Logger.logInfo("You have enabled the FlushOnlyOverWiFi option and there is no WiFi connection, data will not be sent now.");
                } else if (Properties.FILES_PATH != null && !Properties.USER_OPTEDOUT) {
                    for (File file : new File(Properties.FILES_PATH).listFiles(SplunkFileFilter.getInstance())) {
                        NetSenderResponse nsr = new NetSenderResponse(MintUrls.getURL(), null);
                        if (!file.exists()) {
                            nsr.setException(new Exception("There is no data to be sent. This is not an error."));
                            nsr.setSentSuccessfully(Boolean.valueOf(false));
                        } else if (file.length() > 3000000) {
                            file.delete();
                            nsr.setException(new Exception("File was too big, this shouldn't have happened since we split the data."));
                            nsr.setSentSuccessfully(Boolean.valueOf(false));
                        } else {
                            String jsonData = null;
                            try {
                                jsonData = Utils.readFile(file.getAbsolutePath());
                            } catch (Exception e) {
                                nsr.setException(e);
                                nsr.setSentSuccessfully(Boolean.valueOf(false));
                                e.printStackTrace();
                                if (Mint.mintCallback != null) {
                                    Mint.mintCallback.netSenderResponse(nsr);
                                }
                            }
                            if (jsonData == null || jsonData.length() == 0) {
                                if (Mint.mintCallback != null) {
                                    Mint.mintCallback.netSenderResponse(nsr);
                                }
                            } else if (new NetSender().sendBlocking(null, jsonData, false).getSentSuccessfully().booleanValue()) {
                                file.delete();
                            }
                        }
                    }
                }
            }
        });
        if (getExecutor() != null) {
            getExecutor().execute(t);
        }
    }

    public ExecutorService getExecutor() {
        if (executor == null) {
            executor = Executors.newFixedThreadPool(1);
        }
        return executor;
    }
}
