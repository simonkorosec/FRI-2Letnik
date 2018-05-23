import QtQuick 2.9
import QtQuick.Window 2.2
import QtQuick.Controls 2.2

Window {
    id: mainWindow
    visible: true
    width: 1280
    height: 800

    Button {
        id: playBtn
        text: "Igraj"
        font.capitalization: Font.AllUppercase
        font.wordSpacing: 1
        font.letterSpacing: 0
        padding: 12
        font.pointSize: 34
        anchors.verticalCenter: parent.verticalCenter
        anchors.horizontalCenter: parent.horizontalCenter

        onClicked: {
            console.log("Odpri choseWord.qml")
        }

    }

    Image {
        id: settingsBtn
        x: 1208
        width: 47
        height: 42
        fillMode: Image.PreserveAspectFit
        source: "./images/settings.png"
        anchors.top: parent.top
        anchors.topMargin: 25
        anchors.right: parent.right
        anchors.rightMargin: 25

        MouseArea {
            id: settingsBtnMA
            anchors.fill: parent
            pressAndHoldInterval: 5000  // 5sec default value

            onPressAndHold: {
                console.log("Odpri settings.qml")
            }
        }
    }


    Component.onCompleted: {
        var request = new XMLHttpRequest()
        request.open('GET', './json/settings.json');
        request.onreadystatechange = function() {
            if (request.readyState === XMLHttpRequest.DONE) {
                if (request.status && request.status === 200) {
                    var result = JSON.parse(request.responseText);
                    var color = result["bgColor"];
                    mainWindow.color = color;

                    var hold = result["settingPressAndHoldInterval"];
                    settingsBtnMA.pressAndHoldInterval = hold;
                } else {
                    console.log("HTTP:", request.status, request.statusText)
                }
            }
        }
        request.send()

    }

}
