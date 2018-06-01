import QtQuick 2.9
import QtQuick.Window 2.2
import QtQuick.Controls 2.2
import QtMultimedia 5.8
import QtQuick.Dialogs 1.3

Rectangle {
    id: settingsWin
    visible: true
    width: 1280
    height: 800
    property string bgColor
    property real settingPressAndHoldInterval
    property string fontCapitalization
    property real stopnja
    property string barvaPloscic
    property string barvaCrk
    property string colorCorrect
    property string colorWrong
    property bool palyBgMusic
    property real divider: 5

    Button {
        id: playBtn
        text: "Shrani"
        font.capitalization: Font.AllUppercase
        font.wordSpacing: 1
        font.letterSpacing: 0
        padding: 5
        font.pointSize: 21
//            anchors.top: parent.top
//            anchors.topMargin: 15
//            anchors.left: parent.left
//            anchors.leftMargin: 15

        x: 0
        y: 0

        onClicked: {
            correctMove.play();
            saveSettings();
        }

    }

    Rectangle {
        id: container
        anchors.right: parent.right
        anchors.rightMargin: 250
        anchors.left: parent.left
        anchors.leftMargin: 250
        anchors.bottom: parent.bottom
        anchors.bottomMargin: 0
        anchors.top: parent.top
        anchors.topMargin: 0

        ScrollBar {
            id: vbar
            hoverEnabled: true
            active: hovered || pressed
            orientation: Qt.Vertical
            size: 0.01
            width: parent.width - 200
            anchors.top: parent.top
            anchors.right: parent.right
            anchors.bottom: parent.bottom
        }

        Rectangle {
            id: rect1
            height: settingsWin.height / divider
            width: parent.width
            color: parent.color
            y: -vbar.position * height * 3.2

            ComboBox {
                id: tezavnost
                x: 540
                y: 80
                currentIndex: 0
                model: ListModel {
                    ListElement { text: "1" }
                    ListElement { text: "2"}
                    ListElement { text: "3"}
                }
                width: 200
                anchors.right: parent.right
                anchors.rightMargin: 50
                anchors.verticalCenter: parent.verticalCenter
            }

            Text {
                id: text1
                x: 68
                y: 93
                text: qsTr("Stopnja Težavnosti")
                anchors.verticalCenter: parent.verticalCenter
                verticalAlignment: Text.AlignVCenter
                font.wordSpacing: 1
                font.letterSpacing: 0
                padding: 12
                font.pointSize: 21
            }

        }

        Rectangle {
            width: parent.width
            height: 3
            color: "black"
            anchors.top: rect1.bottom
        }

        Rectangle {
            id: rect2
            color: parent.color
            anchors.top: rect1.bottom
            anchors.topMargin: 5
            width: parent.width
            height: settingsWin.height / divider

            ComboBox {
                id: fontCapt
                x: 540
                y: 80
                width: 200
                anchors.verticalCenter: parent.verticalCenter
                currentIndex: 0
                anchors.right: parent.right
                anchors.rightMargin: 50
                model: ListModel {
                    ListElement {
                        text: "Velike"
                        //value: "Font.AllUppercase"
                    }

                    ListElement {
                        text: "Male"
                        //value: "Font.AllLowercase"
                    }
                }
            }

            Text {
                id: text2
                x: 68
                y: 93
                text: qsTr("Kapitalizacija Črk")
                anchors.verticalCenter: parent.verticalCenter
                font.pointSize: 21
                verticalAlignment: Text.AlignVCenter
                font.letterSpacing: 0
                padding: 12
                font.wordSpacing: 1
            }
        }

        Rectangle {
            width: parent.width
            height: 3
            color: "black"
            anchors.top: rect2.bottom
        }

        Rectangle {
            id: rect3
            color: parent.color
            width: parent.width
            height: settingsWin.height / divider
            anchors.top: rect2.bottom
            anchors.topMargin: 5

            Text {
                id: text3
                x: 68
                y: 93
                text: qsTr("Barva Ozadja")
                anchors.verticalCenter: parent.verticalCenter
                font.pointSize: 21
                verticalAlignment: Text.AlignVCenter
                font.letterSpacing: 0
                padding: 12
                font.wordSpacing: 1
            }

            Rectangle{
                id: colorOzadja
                x: 540
                anchors.verticalCenter: parent.verticalCenter
                width: 200
                height: 40
                border.color: "black"

                ColorDialog {
                    id: colorDialogOzadje
                    title: "Please choose a color"
                    onAccepted: {
                        console.log("You chose: " + colorDialogOzadje.color);
                        bgColor = colorDialogOzadje.color;
                        colorOzadja.color = colorDialogOzadje.color;
                    }
                    onRejected: {
                        console.log("Canceled")
                    }
                }

                MouseArea {
                    anchors.fill: parent
                    onClicked: colorDialogOzadje.visible = true
                }
            }
        }

        Rectangle {
            width: parent.width
            height: 3
            color: "black"
            anchors.top: rect3.bottom
        }

        Rectangle {
            id: rect4
            color: parent.color
            width: parent.width
            height: settingsWin.height / divider
            anchors.top: rect3.bottom
            anchors.topMargin: 5

            Text {
                id: text4
                x: 68
                y: 93
                text: qsTr("Barva Ploščic")
                anchors.verticalCenter: parent.verticalCenter
                font.pointSize: 21
                verticalAlignment: Text.AlignVCenter
                font.letterSpacing: 0
                padding: 12
                font.wordSpacing: 1
            }

            Rectangle{
                id: colorPloscica
                x: 540
                //y: 93
                anchors.verticalCenter: parent.verticalCenter
                width: 200
                height: 40
                border.color: "black"

                ColorDialog {
                    id: colorDialogPloscica
                    title: "Please choose a color"
                    onAccepted: {
                        console.log("You chose: " + colorDialogPloscica.color);
                        barvaPloscic = colorDialogPloscica.color;
                        colorPloscica.color = colorDialogPloscica.color;
                    }
                    onRejected: {
                        console.log("Canceled")
                    }
                    //Component.onCompleted: visible = true
                }

                MouseArea {
                    anchors.fill: parent
                    onClicked: colorDialogPloscica.visible = true
                }
            }
        }

        Rectangle {
            width: parent.width
            height: 3
            color: "black"
            anchors.top: rect4.bottom
        }

        Rectangle {
            id: rect5
            color: parent.color
            width: parent.width
            height: settingsWin.height / divider
            anchors.top: rect4.bottom
            anchors.topMargin: 5

            Text {
                id: text5
                x: 68
                y: 93
                text: qsTr("Barva Črk")
                anchors.verticalCenter: parent.verticalCenter
                font.pointSize: 21
                verticalAlignment: Text.AlignVCenter
                font.letterSpacing: 0
                padding: 12
                font.wordSpacing: 1
            }

            Rectangle{
                id: colorCrk
                x: 540
                anchors.verticalCenter: parent.verticalCenter
                width: 200
                height: 40
                border.color: "black"

                ColorDialog {
                    id: colorDialogCrk
                    title: "Please choose a color"
                    onAccepted: {
                        console.log("You chose: " + colorDialogCrk.color);
                        barvaCrk = colorDialogCrk.color;
                        colorCrk.color = colorDialogCrk.color;
                    }
                    onRejected: {
                        console.log("Canceled")
                    }
                    //Component.onCompleted: visible = true
                }

                MouseArea {
                    anchors.fill: parent
                    onClicked: colorDialogCrk.visible = true
                }
            }
        }

        Rectangle {
            width: parent.width
            height: 3
            color: "black"
            anchors.top: rect5.bottom
        }

        Rectangle {
            id: rect6
            color: parent.color
            width: parent.width
            height: settingsWin.height / 5
            anchors.top: rect5.bottom
            anchors.topMargin: 5

            Text {
                id: text6
                x: 68
                y: 93
                text: qsTr("Barva Pravilnih Ploščic")
                anchors.verticalCenter: parent.verticalCenter
                font.pointSize: 21
                verticalAlignment: Text.AlignVCenter
                font.letterSpacing: 0
                padding: 12
                font.wordSpacing: 1
            }

            Rectangle{
                id: colorCorrectR
                x: 540
                anchors.verticalCenter: parent.verticalCenter
                width: 200
                height: 40
                border.color: "black"

                ColorDialog {
                    id: colorDialogCorrect
                    title: "Please choose a color"
                    onAccepted: {
                        console.log("You chose: " + colorDialogCorrect.color);
                        colorCorrect = colorDialogCorrect.color;
                        colorCorrectR.color = colorDialogCorrect.color;
                    }
                    onRejected: {
                        console.log("Canceled")
                    }
                    //Component.onCompleted: visible = true
                }

                MouseArea {
                    anchors.fill: parent
                    onClicked: colorDialogCorrect.visible = true
                }
            }
        }

        Rectangle {
            width: parent.width
            height: 3
            color: "black"
            anchors.top: rect6.bottom
        }

        Rectangle {
            id: rect7
            color: parent.color
            width: parent.width
            height: settingsWin.height / 5
            anchors.top: rect6.bottom
            anchors.topMargin: 5

            Text {
                id: text7
                x: 68
                y: 93
                text: qsTr("Barva Napačnih Ploščic")
                anchors.verticalCenter: parent.verticalCenter
                font.pointSize: 21
                verticalAlignment: Text.AlignVCenter
                font.letterSpacing: 0
                padding: 12
                font.wordSpacing: 1
            }

            Rectangle{
                id: colorWrongR
                x: 540
                anchors.verticalCenter: parent.verticalCenter
                width: 200
                height: 40
                border.color: "black"

                ColorDialog {
                    id: colorDialogWrong
                    title: "Please choose a color"
                    onAccepted: {
                        console.log("You chose: " + colorDialogWrong.color);
                        colorWrong = colorDialogWrong.color;
                        colorWrongR.color = colorDialogWrong.color;
                    }
                    onRejected: {
                        console.log("Canceled")
                    }
                    //Component.onCompleted: visible = true
                }

                MouseArea {
                    anchors.fill: parent
                    onClicked: colorDialogWrong.visible = true
                }
            }
        }

        Rectangle {
            width: parent.width
            height: 3
            color: "black"
            anchors.top: rect7.bottom
        }

        Rectangle {
            id: rect8
            color: parent.color
            width: parent.width
            height: settingsWin.height / divider
            anchors.top: rect7.bottom
            anchors.topMargin: 5

            Text {
                id: text8
                x: 68
                y: 93
                text: qsTr("Glasba v Ozadju")
                anchors.verticalCenter: parent.verticalCenter
                font.pointSize: 21
                verticalAlignment: Text.AlignVCenter
                font.letterSpacing: 0
                padding: 12
                font.wordSpacing: 1
            }

            ComboBox {
                id: glasba
                x: 540
                y: 80
                currentIndex: 2
                model: ListModel {
                    ListElement { text: "Da" }
                    ListElement { text: "Ne" }
                }
                width: 200
                anchors.right: parent.right
                anchors.rightMargin: 50
                anchors.verticalCenter: parent.verticalCenter
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

                    settingsWin.color = result["bgColor"];
                    container.color = result["bgColor"];
                    bgColor = result["bgColor"];
                    settingPressAndHoldInterval = result["settingPressAndHoldInterval"];
                    settingsWin.fontCapitalization = result["fontCapitalization"];
                    stopnja = result["stopnja"];
                    barvaPloscic = result["barvaPloscic"];
                    barvaCrk = result["barvaCrk"];
                    settingsWin.colorCorrect = result["colorCorrect"];
                    settingsWin.colorWrong = result["colorWrong"];
                    palyBgMusic = result["palyBgMusic"];

                    colorOzadja.color = bgColor;
                    colorPloscica.color = barvaPloscic;
                    colorCrk.color = barvaCrk;
                    colorCorrectR.color = settingsWin.colorCorrect;
                    colorWrongR.color = settingsWin.colorWrong;

                    tezavnost.currentIndex = tezavnost.find(stopnja);
                    if (settingsWin.fontCapitalization === "Font.AllUppercase"){
                        fontCapt.currentIndex = 0;
                    } else {
                        fontCapt.currentIndex = 1;
                    }

                    if (palyBgMusic){
                        glasba.currentIndex = 0;
                    } else {
                        glasba.currentIndex = 1;
                    }
                } else {
                    console.log("HTTP:", request.status, request.statusText)
                }
            }
        }
        request.send()
    }

    SoundEffect {
        id: correctMove
        source: "sounds/correct-move.wav"
    }

    function saveSettings() {
        var capt = (fontCapt.currentText === "Velike") ? "Font.AllUppercase" : "Font.AllLowercase";
        var glas = (glasba.currentText === "Da") ? true : false;


        var settings = {
            "bgColor": bgColor,
            "settingPressAndHoldInterval": 3000,
            "fontCapitalization": capt,
            "stopnja": Number(tezavnost.currentText),
            "barvaPloscic": barvaPloscic,
            "barvaCrk": barvaCrk,
            "colorCorrect": colorCorrect,
            "colorWrong": colorWrong,
            "palyBgMusic": glas
        };


        var str = JSON.stringify(settings);
        //console.log(str);

        var request = new XMLHttpRequest();
        request.open("PUT", "json/settings.json", false);
        request.send(str);
        console.log(request.status);

        mainWindow.readSettings();
        settingsWin.destroy(10);
    }
}
