from __future__ import print_function

import pexpect


def test_count():
    baza = pexpect.pexpect()

    try:
        baza.expect("command>")
        baza.send("count")
        baza.expect(">> No. of persons: 0")

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
        baza.send("reset")
        baza.expect("reset> Are you sure(y|n): ")
        baza.send("n")

        baza.expect("command>")
        baza.send("count")
        baza.expect(">> No. of persons: 1")

        baza.expect("command>")
        baza.send("reset")
        baza.expect("reset> Are you sure(y|n): ")
        baza.send("y")
        baza.expect(">> OK")

        baza.expect("command>")
        baza.send("count")
        baza.expect(">> No. of persons: 0")

        print("PASSED\ttest_count")

    except:
        print("FAILED\ttest_count")

    finally:
        baza.kill()


def run_all():
    test_count()


if __name__ == "__main__":
    run_all()
