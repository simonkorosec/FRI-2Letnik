import QtQuick 2.0
import QtMultimedia 5.8

Rectangle {
    id: choseWord
    width: 1280
    height: 800
    property real stopnja: 1
    property bool lvlOpened: false

    ListModel {
        id: besedaModel
    }

    Component {
        id: besedaDelegate
        Item {
            width: mreza.cellWidth; height: mreza.cellHeight
            anchors.horizontalCenter: parent.horizontalCenter
            anchors.horizontalCenterOffset: 85
            Column{
                anchors.verticalCenter: parent.verticalCenter
                anchors.leftMargin: 50
                Text {
                    text: tekst
                    font.pointSize: 38
                    font.capitalization: capitalization
                    anchors.horizontalCenter: parent.horizontalCenter
                }
            }

            Column {
                //anchors.fill: parent
                anchors.right: parent.right
                anchors.rightMargin: 50
                anchors.verticalCenter: parent.verticalCenter

                Image {
                    width: 200
                    height: 200
                    fillMode: Image.PreserveAspectFit
                    source: slika
                    anchors.horizontalCenter: parent.horizontalCenter
                }
            }

            MouseArea {
                anchors.fill: parent

                onClicked: {
                    openLevel(tekst);
                }
            }
        }
    }

    GridView {
        id: mreza
        width: 930
        anchors.top: parent.top
        anchors.topMargin: 8
        anchors.bottom: parent.bottom
        anchors.bottomMargin: 8
        anchors.horizontalCenter: parent.horizontalCenter
        anchors.horizontalCenterOffset: -80

        cellWidth: parent.width / 1.9
        cellHeight: 250

        model: besedaModel
        delegate: besedaDelegate
        //highlight: Rectangle { color: "lightsteelblue"; radius: 5 }
        focus: true
    }

    Component.onCompleted: {
        readSettings();
    }

    function readSettings(){
        var settings = new XMLHttpRequest();
        settings.open('GET', './json/settings.json');
        settings.onreadystatechange = function() {
            if (settings.readyState === XMLHttpRequest.DONE) {
                if (settings.status && settings.status === 200) {
                    //console.log("response", settings.responseText)
                    var result = JSON.parse(settings.responseText);
                    var color = result["bgColor"];
                    choseWord.color = color;
                    var cap = result["fontCapitalization"];

                    choseWord.stopnja = result["stopnja"];

                    readWords(cap);
                } else {
                    console.log("HTTP:", settings.status, settings.statusText);
                }
            }
        }
        settings.send();
    }

    function readWords(capitalization){
        if (capitalization === "Font.AllUppercase"){
            capitalization = Font.AllUppercase;
        } else {
            capitalization = Font.AllLowercase;
        }

        var request = new XMLHttpRequest();
        request.open('GET', './json/words.json');
        request.onreadystatechange = function() {
            if (request.readyState === XMLHttpRequest.DONE) {
                if (request.status && request.status === 200) {
                    //console.log("response", request.responseText)
                    var result = JSON.parse(request.responseText);
                    var list = result["slike"];
                    for (var i in list) {
                        besedaModel.append({
                                               "slika": list[i].slika,
                                               "tekst": list[i].tekst,
                                               "capitalization": capitalization
                                           })
                    }
                } else {
                    console.log("HTTP:", request.status, request.statusText);
                }
            }
        }
        request.send();
    }

    function openLevel(tekst){
        if (lvlOpened) {
            return;
        }
        correctMove.play();

        lvlOpened = true;

        var component;
        if (stopnja === 1) {
            component = Qt.createComponent("playLevel1.qml");
        } else if (stopnja === 2) {
            component = Qt.createComponent("playLevel2.qml");
        } else if (stopnja === 3) {
            component = Qt.createComponent("playLevel3.qml");
        } else if (stopnja === 4) {
            component = Qt.createComponent("playLevel4.qml");
        }

        var playLevel = component.createObject(choseWord, {"x": 0, "y": 0, "beseda": tekst});
        //choseWord.destroy(20);
        if (playLevel === null) {
            // Error Handling
            console.log("Error creating object");
        }
    }


    SoundEffect {
            id: correctMove
            source: "sounds/correct-move.wav"
        }

}


