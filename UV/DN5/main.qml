import QtQuick 2.9
import QtQuick.Window 2.2
import QtQuick.Controls 2.2
import QtMultimedia 5.8

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
            //console.log("Odpri choseWord.qml");
            correctMove.play();
            openChoseWord();
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
                console.log("Odpri settings.qml");
                correctMove.play();
                openSettings();
            }
        }
    }


    Component.onCompleted: {
readSettings();
    }

    function readSettings() {
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

                    var glasba = result["palyBgMusic"];
                    if (glasba){
                        bgMusic.play();
                    } else {
                        bgMusic.stop();
                    }

                } else {
                    console.log("HTTP:", request.status, request.statusText)
                }
            }
        }
        request.send()

    }

    function openChoseWord(){
        var component = Qt.createComponent("choseWord.qml");
        var choseWord = component.createObject(mainWindow, {"x": 0, "y": 0});
        if (choseWord === null) {
            // Error Handling
            console.log("Error creating object");
        }
    }

    function openSettings(){
        var component = Qt.createComponent("settings.qml");
        var choseWord = component.createObject(mainWindow, {"x": 0, "y": 0});
        if (choseWord === null) {
            // Error Handling
            console.log("Error creating object");
        }
    }

    SoundEffect {
        id: correctMove
        source: "sounds/correct-move.wav"
    }

    SoundEffect {
        id: bgMusic
        source: "sounds/calm-music.wav"
        volume: 0.5
    }
}
