from __future__ import print_function

import os
import stat
import pexpect_imenik


def test_bst_save_no_perm():
    baza = pexpect_imenik.pexpect()
    i_filename = "i_test.bin"
    t_filename = "t_test.bin"

    try:
        fd = open(i_filename, "w")
        fd.close()

        ft = open(t_filename, "w")
        ft.close()

        mode = os.stat(i_filename)[stat.ST_MODE]
        os.chmod(i_filename, mode & ~stat.S_IWRITE)
        mode = os.stat(t_filename)[stat.ST_MODE]
        os.chmod(t_filename, mode & ~stat.S_IWRITE)

        baza.expect("Enter command: ")

        baza.send("use bst")
        baza.expect("OK")
        baza.expect("Enter command: ")

        baza.send("add Andrej Novak 013456789")
        baza.expect("OK")
        baza.expect("Enter command: ")

        baza.send("add Janez Levak 012345678")
        baza.expect("OK")
        baza.expect("Enter command: ")

        baza.send("print")
        baza.expect("Novak, Andrej - 013456789")
        baza.expect("\tLevak, Janez - 012345678")
        baza.expect("OK")
        baza.expect("Enter command: ")

        baza.send("save test.bin")
        baza.expect("Error: IO error i_test.bin (Access is denied)")
        baza.expect("Enter command: ")

        print("PASSED\ttest_bst_save_no_perm")

    except:
        print("FAILED\ttest_bst_save_no_perm")

    finally:
        baza.kill()
        os.chmod(i_filename, stat.S_IWRITE)
        os.chmod(t_filename, stat.S_IWRITE)
        os.remove(i_filename)
        os.remove(t_filename)


if __name__ == "__main__":
    test_bst_save_no_perm()
