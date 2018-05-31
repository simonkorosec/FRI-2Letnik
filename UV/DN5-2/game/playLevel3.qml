import QtQuick 2.0

Rectangle {
    id: lvl3
    width: 1280
    height: 800
    property string barva: "black"
    property string barvaPloscic: "red"
    property string barvaCrk: "white"
    property string beseda: "avto"

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
                Text {
                    text: tekst
                    font.pointSize: 38
                    font.capitalization: capitalization
                    anchors.horizontalCenter: parent.horizontalCenter
                }
            }

            Column {
                anchors.fill: parent
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
                    console.log("Stopnja: " + lvl3.stopnja + " Beseda: " + tekst);
                }
            }
        }
    }

    GridView {
        id: mreza
        width: 921
        anchors.top: parent.top
        anchors.topMargin: 8
        anchors.bottom: parent.bottom
        anchors.bottomMargin: 8
        anchors.horizontalCenter: parent.horizontalCenter
        anchors.horizontalCenterOffset: 27

        cellWidth: parent.width / 1.9
        cellHeight: 250

        model: besedaModel
        delegate: besedaDelegate
//        highlight: Rectangle { color: "lightsteelblue"; radius: 5 }
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
                    lvl3.color = color;
                    var cap = result["fontCapitalization"];

                    lvl3.stopnja = result["stopnja"];

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

}


