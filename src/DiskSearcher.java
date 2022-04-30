//ID:319024303

import java.io.File;

public class DiskSearcher {

    public static final int AUDITING_QUEUE_CAPACITY = 5000;
    public static final int DIRECTORY_QUEUE_CAPACITY = 5000;
    public static final int RESULTS_QUEUE_CAPACITY = 5000;

    public static void main(String[] args) {
        long start = System.currentTimeMillis();
        //Checks if all parameters are inserted.
        if (args.length != 6) {
            System.out.println("Invalid number of arguments");
            System.exit(0);
        }
        //Init queues
        SynchronizedQueue<String> milestonesQueue = args[0].equals("false") ? null : new SynchronizedQueue<>(AUDITING_QUEUE_CAPACITY);
        SynchronizedQueue<File> resultsQueue = new SynchronizedQueue(RESULTS_QUEUE_CAPACITY);
        SynchronizedQueue<File> directoryQueue = new SynchronizedQueue(DIRECTORY_QUEUE_CAPACITY);
        //Init first log message.
        if(milestonesQueue!=null){
            milestonesQueue.enqueue("General, program has started the search");
        }
        //Init parameters from the command line.
        boolean isAudit = Boolean.parseBoolean(args[0]);
        String prefix = args[1];
        File root = new File(args[2]);
        File destination = new File(args[3]);
        int number_of_searchers = Integer.parseInt(args[4]);
        int number_of_copiers = Integer.parseInt(args[5]);

        //Init Scouter
        Scouter scout = new Scouter(1, directoryQueue, root, milestonesQueue, isAudit);
        Thread scout_thread = new Thread(scout);
        //Starting Scout thread
        scout_thread.start();

        //Init Searchers and starting their threads.
        Searcher[] searchers = new Searcher[number_of_searchers];
        Thread[] searchers_threads = new Thread[number_of_searchers];
        for (int i = 0; i < number_of_searchers; i++) {
            searchers[i] = new Searcher(i, prefix, directoryQueue, resultsQueue, milestonesQueue, isAudit);
            searchers_threads[i] = new Thread(searchers[i]);
            searchers_threads[i].start();
        }

        //Init Copiers and starting their threads.
        Copier[] copiers = new Copier[number_of_copiers];
        Thread[] copiers_threads = new Thread[number_of_copiers];
        for (int i = 0; i < number_of_copiers; i++) {
            copiers[i] = new Copier(i, destination, resultsQueue, milestonesQueue, isAudit);
            copiers_threads[i] = new Thread(copiers[i]);
            copiers_threads[i].start();
        }
        //Waiting for all threads to finish.
        try {
            //Waiting for scout thread
            scout_thread.join();
            //Waiting for Searchers.
            for (int i = 0; i < number_of_searchers; i++) {
                searchers_threads[i].join();

            }
            //Waiting for Copiers.
            for (int i = 0; i < number_of_copiers; i++) {
                copiers_threads[i].join();

            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        long finish = System.currentTimeMillis();
        int i = 0;
        //Printing the log file.
        if (isAudit) {
            while (milestonesQueue.getSize() != 0) {
                String message = milestonesQueue.dequeue();
                System.out.println("Message " + i + ": " + message);
                i++;
            }
        }
        System.out.println("Total run time for the progrem is: " + (finish - start) + " ms");
    }
}
