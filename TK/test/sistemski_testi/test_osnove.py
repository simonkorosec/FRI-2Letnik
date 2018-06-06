from __future__ import print_function

import pexpect


def test_exit():
    baza = pexpect.pexpect()

    try:
        baza.expect("command>")

        baza.send("exit")
        baza.expect(">> Goodbye")
        print("PASSED\ttest_exit")

    except:
        print("FAILED\ttest_exit")

    finally:
        baza.kill()


def test_invalid_cmd():
    baza = pexpect.pexpect()

    try:
        baza.expect("command>")

        baza.send("use bst")
        baza.expect(">> Invalid command")
        print("PASSED\ttest_invalid_cmd")

    except:
        print("FAILED\ttest_invalid_cmd")

    finally:
        baza.kill()


def test_invalid_arg():
    baza = pexpect.pexpect()

    try:
        baza.expect("command>")

        baza.send("use bst")
        baza.expect(">> Invalid command")
        print("PASSED\ttest_invalid_arg")

    except:
        print("FAILED\ttest_invalid_arg")

    finally:
        baza.kill()


def run_all():
    test_exit()
    test_invalid_cmd()


if __name__ == "__main__":
    run_all()
