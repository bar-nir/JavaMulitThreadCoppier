//ID:319024303

import java.io.*;

public class Copier implements Runnable {

    public static final int COPY_BUFFER_SIZE = 4096;
    private File destination;
    private SynchronizedQueue<File> resultsQueue;
    private SynchronizedQueue<String> auditingQueue;
    private boolean isAudit;
    private int id;

    /**
     * @param id            Thread id
     * @param destination   File directory write the copied files.
     * @param resultsQueue  queue with current files to coppy.
     * @param auditingQueue logs queue
     * @param isAudit       logs flag
     */
    public Copier(int id,
                  File destination,
                  SynchronizedQueue<File> resultsQueue,
                  SynchronizedQueue<String> auditingQueue,
                  boolean isAudit) {
        this.id = id;
        this.destination = destination;
        this.resultsQueue = resultsQueue;
        this.auditingQueue = auditingQueue;
        this.isAudit = isAudit;
        if (this.isAudit) {
            this.auditingQueue.registerProducer();
        }
    }

    /**
     * Reading files from the result queue and copy them into destination.
     */
    @Override
    public void run() {

        InputStream in = null;
        OutputStream out = null;
        File item = this.resultsQueue.dequeue();
        //until there is Producer that are register on the directory queue and queue size is not 0 keep popping item from queue.
        // @see dequeue method in SynchronizedQueue.java.
        while (item != null) {
            //Creating new File at the destination.
            File copied = new File(this.destination.getAbsolutePath() + "/" + item.getName());
            try {
                in = new FileInputStream(item);
                out = new FileOutputStream(copied);
                byte[] buffer = new byte[COPY_BUFFER_SIZE];
                int length;
                //Copy the current file into the destination.
                while ((length = in.read(buffer)) > 0) {
                    out.write(buffer, 0, length);
                }
                //Handling logs
                if (isAudit) {
                    this.auditingQueue.enqueue("Copier from thread id " + this.id + ": file named " + copied.toString() + "was copied");
                }
            } catch (Exception e) {
//                System.out.println(e.getMessage());
                item = this.resultsQueue.dequeue();
                continue;
            } finally {
                //Close files
                try {
                    in.close();
                    out.close();
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }

            }
            //Dequeue next file
            item = this.resultsQueue.dequeue();
        }
        //Unregister the Copier from logs file.
        if (this.isAudit) {
            this.auditingQueue.unregisterProducer();
        }
    }
}

