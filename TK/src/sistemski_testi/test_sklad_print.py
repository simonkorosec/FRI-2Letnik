import pexpect


def test_sklad_print():
    baza = pexpect.pexpect()

    try:
        baza.expect("Enter command: ")

        baza.send("use sk")
        baza.expect("OK")
        baza.expect("Enter command: ")

        baza.send("add 1")
        baza.expect("OK")
        baza.expect("Enter command: ")

        baza.send("add 2")
        baza.expect("OK")
        baza.expect("Enter command: ")

        baza.send("print")
        baza.expect("2 1 ")
        baza.expect("OK")
        baza.expect("Enter command: ")

        baza.send("size")
        baza.expect("2")
        baza.expect("Enter command: ")

        print "PASSED\ttest_sklad_print"

    except:
        print "FAILED\ttest_sklad_print"

    finally:
        baza.kill()


if __name__ == "__main__":
    test_sklad_print()

