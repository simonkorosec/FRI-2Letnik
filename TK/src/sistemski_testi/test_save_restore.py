from __future__ import print_function

import os
import stat

import pexpect


def test_save_restore_ok():
    baza = pexpect.pexpect()

    try:
        baza.expect("command>")
        baza.send("add")
        baza.expect("add> EMSO: ")
        baza.send("3105000500232")
        baza.expect("add> IME: ")
        baza.send("Jan Vid")
        baza.expect("add> PRIIMEK: ")
        baza.send("Novak")
        baza.expect("add> STAROST: ")
        baza.send("18")
        baza.expect(">> OK")

        baza.expect("command>")
        baza.send("count")
        baza.expect(">> No. of persons: 1")
        baza.expect("command>")
        baza.send("save test.bin")
        baza.expect(">> OK")

        baza.expect("command>")
        baza.send("reset")
        baza.expect("reset> Are you sure (y|n): ")
        baza.send("y")
        baza.expect(">> OK")

        baza.expect("command>")
        baza.send("count")
        baza.expect(">> No. of persons: 0")

        baza.expect("command>")
        baza.send("restore test.bin")
        baza.expect(">> OK")

        baza.expect("command>")
        baza.send("count")
        baza.expect(">> No. of persons: 1")

        print("PASSED\ttest_save_restore_ok")

    except:
        print("FAILED\ttest_save_restore_ok")

    finally:
        baza.kill()


def test_save_no_perm():
    baza = pexpect.pexpect()
    i_filename = "E_test.bin"  # EMSO
    t_filename = "I_test.bin"  # Ime priimek

    try:
        fd = open(i_filename, "w")
        fd.close()
        ft = open(t_filename, "w")
        ft.close()
        mode = os.stat(i_filename)[stat.ST_MODE]
        os.chmod(i_filename, mode & ~stat.S_IWRITE)
        mode = os.stat(t_filename)[stat.ST_MODE]
        os.chmod(t_filename, mode & ~stat.S_IWRITE)

        baza.expect("command>")
        baza.send("add")
        baza.expect("add> EMSO: ")
        baza.send("3105000500232")
        baza.expect("add> IME: ")
        baza.send("Jan Vid")
        baza.expect("add> PRIIMEK: ")
        baza.send("Novak")
        baza.expect("add> STAROST: ")
        baza.send("18")
        baza.expect(">> OK")

        baza.expect("command>")
        baza.send("count")
        baza.expect(">> No. of persons: 1")
        baza.expect("command>")
        baza.send("save test.bin")
        baza.expect(">> I/O Error: E_test.bin (Access is denied)")

        print("PASSED\ttest_save_no_perm")

    except:
        print("FAILED\ttest_save_no_perm")

    finally:
        baza.kill()
        os.chmod(i_filename, stat.S_IWRITE)
        os.chmod(t_filename, stat.S_IWRITE)
        os.remove(i_filename)
        os.remove(t_filename)


if __name__ == "__main__":
    test_save_restore_ok()
    test_save_no_perm()
