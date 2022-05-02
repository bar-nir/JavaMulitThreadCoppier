# Java multi-threaded application to copy files 

The utility was developed part of operating system course and will allow searching for all files with a specific prefix in a root directory. 
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
* Install the latest version of [Java](https://java.com)
* Clone the repository
```
$ git init
$ git pull git@github.com:bar-nir/JavaMulitThreadCoppier.git
```
* Open the project in IntelliJ
* Set up SDK 15 or above


## Executing program
#### There are 6 command line arguments
- 1 : boolean flag for logs , (True = printing logs) ,(False = not printing logs)
- 2 : Prefix (copy files from given directroy that start with a specific prefix)
- 3 : Root to copy files from.
- 4 : Destenation root for files.
- 5 : Number of searchers threads.
- 6 : Number of coppiers threads.
```
$ DiskSearcher <boolean flag for logs> <file-prefix> <root directory> <dest directroy> <# of searchers> <# of coppiers>
```




