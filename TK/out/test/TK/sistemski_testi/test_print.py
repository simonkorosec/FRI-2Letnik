# coding=utf-8

from __future__ import print_function

import pexpect


def test_print_zero():
    baza = pexpect.pexpect()

    try:
        baza.expect("command>")

        baza.send("print")
        baza.expect(">> No. of persons: 0")

        print("PASSED\ttest_print_zero")

    except:
        print("FAILED\ttest_print_zero")

    finally:
        baza.kill()


def test_print_one():
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
        baza.send("print")
        baza.expect(">> No. of persons: 1")
        baza.expect("\t3105000500232 | Novak, Jan Vid | 18 - lahko voli")

        print("PASSED\ttest_print_one")

    except:
        print("FAILED\ttest_print_one")

    finally:
        baza.kill()


def test_print_three():
    baza = pexpect.pexpect()

    try:
        baza.expect("command>")
        baza.send("add")
        baza.expect("add> EMSO: ")
        baza.send("2111965500138")
        baza.expect("add> IME: ")
        baza.send("Boris")
        baza.expect("add> PRIIMEK: ")
        baza.send("Anderlic")
        baza.expect("add> STAROST: ")
        baza.send("53")
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
        baza.send("add")
        baza.expect("add> EMSO: ")
        baza.send("1310004505091")
        baza.expect("add> IME: ")
        baza.send("Amadeja")
        baza.expect("add> PRIIMEK: ")
        baza.send("Svet")
        baza.expect("add> STAROST: ")
        baza.send("14")
        baza.expect(">> OK")

        baza.expect("command>")
        baza.send("print")
        baza.expect(">> No. of persons: 3")
        baza.expect("\t2111965500138 | Anderlic, Boris | 53 - lahko voli")
        baza.expect("\t3105000500232 | Novak, Jan Vid | 18 - lahko voli")
        baza.expect("\t1310004505091 | Svet, Amadeja | 14 - ne more voliti")

        print("PASSED\ttest_print_three")

    except:
        print("FAILED\ttest_print_three")

    finally:
        baza.kill()


def run_all():
    test_print_zero()
    test_print_one()
    test_print_three()


if __name__ == "__main__":
    run_all()
