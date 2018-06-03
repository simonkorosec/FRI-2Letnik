import pexpect


def test_bst_print():
    baza = pexpect.pexpect()

    try:
        baza.expect("Enter command: ")

        baza.send("use bst")
        baza.expect("OK")
        baza.expect("Enter command: ")

        baza.send("add Andrej Novak 013456789")
        baza.expect("OK")
        baza.expect("Enter command: ")

        baza.send("add Janez Levak 012345678")
        baza.expect("OK")
        baza.expect("Enter command: ")

        baza.send("print")
        baza.expect("Novak, Andrej - 013456789")
        baza.expect("\tLevak, Janez - 012345678")
        baza.expect("OK")
        baza.expect("Enter command: ")

        baza.send("count")
        baza.expect("2")
        baza.expect("Enter command: ")

        baza.send("depth")
        baza.expect("2")
        baza.expect("Enter command: ")

        print("PASSED\ttest_bst_print")

    except:
        print("FAILED\ttest_bst_print")

    finally:
        baza.kill()


if __name__ == "__main__":
    test_bst_print()

