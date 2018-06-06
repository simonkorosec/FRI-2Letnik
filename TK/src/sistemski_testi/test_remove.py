from __future__ import print_function

import pexpect


def test_remove_ok():
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
        baza.send("remove 3105000500232")
        baza.expect(">> OK")

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
        baza.send("remove")
        baza.expect("remove> IME: ")
        baza.send("Jan Vid")
        baza.expect("remove> PRIIMEK: ")
        baza.send("Novak")
        baza.expect(">> OK")

        print("PASSED\ttest_remove_ok")

    except:
        print("FAILED\ttest_remove_ok")

    finally:
        baza.kill()


def test_remove_invalid():
    baza = pexpect.pexpect()

    try:
        baza.expect("command>")

        baza.send("remove 31050005002322")
        baza.expect(">> Invalid input data")

        print("PASSED\ttest_remove_invalid")

    except:
        print("FAILED\ttest_remove_invalid")

    finally:
        baza.kill()


def test_remove_not_exists():
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
        baza.send("remove 3105200500232")
        baza.expect(">> Person does not exist")

        baza.expect("command>")
        baza.send("remove")
        baza.expect("remove> IME: ")
        baza.send("Janko")
        baza.expect("remove> PRIIMEK: ")
        baza.send("Hrib")
        baza.expect(">> Person does not exist")

        print("PASSED\ttest_remove_not_exists")

    except:
        print("FAILED\ttest_remove_not_exists")

    finally:
        baza.kill()


def run_all():
    test_remove_ok()
    test_remove_invalid()
    test_remove_not_exists()


if __name__ == "__main__":
    run_all()
