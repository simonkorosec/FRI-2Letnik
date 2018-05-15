import pexpect


def test_pv_print():
    baza = pexpect.pexpect()

    try:
        baza.expect("Enter command: ")

        baza.send("use pv")
        baza.expect("OK")
        baza.expect("Enter command: ")

        baza.send("add 1")
        baza.expect("OK")
        baza.expect("Enter command: ")

        baza.send("add 2")
        baza.expect("OK")
        baza.expect("Enter command: ")

        baza.send("add 5")
        baza.expect("OK")
        baza.expect("Enter command: ")

        baza.send("print")
        baza.expect("5 1 2 ")
        baza.expect("OK")
        baza.expect("Enter command: ")

        baza.send("size")
        baza.expect("3")
        baza.expect("Enter command: ")

        print "PASSED\ttest_pv_print"

    except:
        print "FAILED\ttest_pv_print"

    finally:
        baza.kill()


if __name__ == "__main__":
    test_pv_print()

