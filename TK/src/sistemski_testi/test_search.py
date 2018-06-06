from __future__ import print_function

import pexpect


def test_search_ok():
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
        baza.send("search 3105000500232")
        baza.expect(">> 3105000500232 | Novak, Jan Vid | 18 - lahko voli")

        baza.expect("command>")
        baza.send("search")
        baza.expect("search> IME: ")
        baza.send("Jan Vid")
        baza.expect("search> PRIIMEK: ")
        baza.send("Novak")
        baza.expect(">> 3105000500232 | Novak, Jan Vid | 18 - lahko voli")

        print("PASSED\ttest_search_ok")

    except:
        print("FAILED\ttest_search_ok")

    finally:
        baza.kill()


def test_search_invalid():
    baza = pexpect.pexpect()

    try:
        baza.expect("command>")

        baza.send("search 131050005002322")
        baza.expect(">> Invalid input data")

        print("PASSED\ttest_search_invalid")

    except:
        print("FAILED\ttest_search_invalid")

    finally:
        baza.kill()


def test_search_not_exists():
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
        baza.send("search 1105200500232")
        baza.expect(">> Person does not exist")

        baza.expect("command>")
        baza.send("search")
        baza.expect("search> IME: ")
        baza.send("Janko")
        baza.expect("search> PRIIMEK: ")
        baza.send("Hrib")
        baza.expect(">> Person does not exist")

        print("PASSED\ttest_search_not_exists")

    except:
        print("FAILED\ttest_search_not_exists")

    finally:
        baza.kill()


def run_all():
    test_search_ok()
    test_search_invalid()
    test_search_not_exists()


if __name__ == "__main__":
    run_all()
