from __future__ import print_function

import pexpect


def test_add_ok():
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

        print("PASSED\ttest_add_ok")

    except:
        print("FAILED\ttest_add_ok")

    finally:
        baza.kill()


def test_add_invalid():
    baza = pexpect.pexpect()

    try:
        baza.expect("command>")

        baza.send("add")
        baza.expect("add> EMSO: ")
        baza.send("31050005002320")
        baza.expect("add> IME: ")
        baza.send("Jan Vid")
        baza.expect("add> PRIIMEK: ")
        baza.send("Novak")
        baza.expect("add> STAROST: ")
        baza.send("18")

        baza.expect(">> Invalid input data")

        print("PASSED\ttest_add_invalid")

    except:
        print("FAILED\ttest_add_invalid")

    finally:
        baza.kill()


def test_add_duplicate():
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

        # Enaka EMSA
        baza.expect("command>")
        baza.send("add")
        baza.expect("add> EMSO: ")
        baza.send("3105000500232")
        baza.expect("add> IME: ")
        baza.send("Jan")
        baza.expect("add> PRIIMEK: ")
        baza.send("Hrib")
        baza.expect("add> STAROST: ")
        baza.send("18")
        baza.expect(">> Person already exists")

        # Enaka Ime Priimek
        baza.expect("command>")
        baza.send("add")
        baza.expect("add> EMSO: ")
        baza.send("2105000500232")
        baza.expect("add> IME: ")
        baza.send("Jan Vid")
        baza.expect("add> PRIIMEK: ")
        baza.send("Novak")
        baza.expect("add> STAROST: ")
        baza.send("18")
        baza.expect(">> Person already exists")

        print("PASSED\ttest_add_duplicate")

    except:
        print("FAILED\ttest_add_duplicate")

    finally:
        baza.kill()


def run_all():
    test_add_ok()
    test_add_invalid()
    test_add_duplicate()


if __name__ == "__main__":
    run_all()
