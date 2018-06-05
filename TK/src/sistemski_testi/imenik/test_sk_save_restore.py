import pexpect_imenik


def test_sk_save_restore():
    baza = pexpect_imenik.pexpect()

    try:
        baza.expect("Enter command: ")

        baza.send("use sk")
        baza.expect("OK")
        baza.expect("Enter command: ")

        baza.send("add Janez Levak 012345678")
        baza.expect("OK")
        baza.expect("Enter command: ")

        baza.send("add Andrej Novak 013456789")
        baza.expect("OK")
        baza.expect("Enter command: ")

        baza.send("add Janez Novak 014567890")
        baza.expect("OK")
        baza.expect("Enter command: ")

        baza.send("print")
        baza.expect("Novak, Janez - 014567890")
        baza.expect("Novak, Andrej - 013456789")
        baza.expect("Levak, Janez - 012345678")
        baza.expect("OK")
        baza.expect("Enter command: ")

        baza.send("save test.bin")
        baza.expect("OK")
        baza.expect("Enter command: ")

        baza.send("reset")
        baza.expect("OK")
        baza.expect("Enter command: ")

        baza.send("print")
        baza.expect("OK")
        baza.expect("Enter command: ")

        baza.send("count")
        baza.expect("0")
        baza.expect("Enter command: ")

        baza.send("restore test.bin")
        baza.expect("OK")
        baza.expect("Enter command: ")

        baza.send("print")
        baza.expect("Novak, Janez - 014567890")
        baza.expect("Novak, Andrej - 013456789")
        baza.expect("Levak, Janez - 012345678")
        baza.expect("OK")
        baza.expect("Enter command: ")

        print("PASSED\ttest_bst_save_restore")

    except:
        print("FAILED\ttest_bst_save_restore")

    finally:
        baza.kill()


if __name__ == "__main__":
    test_sk_save_restore()
