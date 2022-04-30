# Java multi-threaded application to copy files 

The utility will allow searching for all files with a specific prefix in a root directory. 
Files with the specific prefix will be copied to a specified directory.

## Description

#### Class SynchronizedQueue
This class should allow multithreaded enqueue/dequeue operations.

#### Class Scouter
This class is responsible for listing all directories that exist under the given root directory. 
It enqueues all directories into the directory queue.
There is always only one scouter thread in the system.

#### Class Searcher
This class reads a directory from the directory queue and lists all files in this directory. 
Then, it checks for each file name if it has the correct prefix. 
Files that have the correct prefix are enqueued to the results queue (to be copied).

#### Class copier
This class reads a file from the results queue (the queue of files that contains the output of the searchers), and copies it into the specified destination directory.

#### Class DiskSearcher
This is the main class of the application. This class contains a main method that starts the search process according to the given command lines.
The main class needs to contain constant variables that will tell the limit of files that need to be copied (a maximum amount, a threshold).

## Installing

* Install [IntelliJ](https://www.jetbrains.com/help/idea/installation-guide.html)
* Clone the project git clone git@github.com:bar-nir/JavaMulitThreadCoppier.git
* Open the project in IntelliJ
* Set up SDK 15 or above


## Executing program
#### There are 6 inputs parameters.
- Logs flag : boolean for priting the threads logs.
- Prefix : copy files with that starts with this prefix.
- Root : root to copy files from.
- Dest : destenation for files.
- Number of searchers threads.
- Number of coppiers threads.
```
$ DiskSearcher <boolean flag for logs> <file-prefix> <root directory> <dest directroy> <# of searchers> <# of coppiers>
```




