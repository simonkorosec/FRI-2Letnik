from __future__ import print_function

import os
import subprocess
import threading
import time

try:
    import queue as Queue
except:
    import Queue


def enqueue_output(out, queue):
    c = out.read(1)
    while 0 < len(c):
        queue.put(c)
        c = out.read(1)


class pexpect:
    def __init__(self):
        commandLine = ["java",
                       "-cp",
                       "C:\\Users\\" + os.environ['USERNAME'] + "\\Documents\\FRI-2Letnik\\TK\\out\\production\\TK",
                       "imenik/PodatkovnaBaza"]

        self.process = subprocess.Popen(commandLine,
                                        stdin=subprocess.PIPE,
                                        stdout=subprocess.PIPE,
                                        stderr=subprocess.STDOUT)
        self.queue = Queue.Queue()
        self.thread = threading.Thread(target=enqueue_output, args=(self.process.stdout, self.queue))
        self.thread.start()
        self.killable = True

    def __del__(self):
        self.kill()

    def kill(self):
        if self.killable:
            if self.process.poll() is None:
                self.process.kill()
            self.thread.join()
            self.killable = False

    def expect(self, expected_string):
        actualString = ""
        readRetries = 0

        while self.queue.empty():
            time.sleep(0.1)
            ++readRetries
            if readRetries > 100:
                self.kill()
                assert False

        while not self.queue.empty():
            actualString += self.queue.get_nowait()
            if actualString[-1] == '\n':
                break

        actualString = actualString.strip('\n\r')
        if not actualString == expected_string:
            print("\nERROR: Wrong output received:\n\tExpected: '%s'\n\tActual:   '%s'\n" % (
                expected_string, actualString))
            self.kill()
            assert False

    def send(self, inputString):
        self.process.stdin.write(inputString + "\n")
