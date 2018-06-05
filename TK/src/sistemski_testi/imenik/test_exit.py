from __future__ import print_function

import pexpect_imenik


def test_exit_no_use():
    baza = pexpect_imenik.pexpect()

    try:
        baza.expect("Enter command: ")

        baza.send("exit")
        baza.expect("Have a nice day.")
        print("PASSED\ttest_exit_no_use")

    except:
        print("FAILED\ttest_exit_no_use")

    finally:
        baza.kill()


def test_exit_use_bst():
    baza = pexpect_imenik.pexpect()

    try:
        baza.expect("Enter command: ")

        baza.send("use bst")
        baza.expect("OK")
        baza.expect("Enter command: ")

        baza.send("exit")
        baza.expect("Have a nice day.")
        print("PASSED\ttest_exit_use_bst")

    except:
        print("FAILED\ttest_exit_use_bst")

    finally:
        baza.kill()


if __name__ == "__main__":
    test_exit_no_use()
    test_exit_use_bst()

