import QtQuick 2.0
import QtQuick.Layouts 1.3

Rectangle {
    id: lvl2
    color: "lightgrey"
    width: 1280
    height: 800
    property string barva: "black"
    property string barvaPloscic: "red"
    property string barvaCrk: "white"
    property string beseda: "avto"


    ColumnLayout {
        anchors.fill: parent

        RowLayout {
            ColumnLayout {
                Rectangle {
                    id: pomoc
                    color: barva  //"blue"
                    Layout.preferredHeight: lvl2.height/3
                    Layout.fillWidth: true

                    Text {
                        id: pomocTekst
                        anchors.centerIn: parent
                        text: "napaka"
                        font.pointSize: 24
                        font.capitalization: Font.AllUppercase
                        font.letterSpacing: 16
                    }
                }
                Rectangle {
                    id: vnos
                    color: barva //"green"
                    Layout.preferredHeight: lvl2.height/3
                    Layout.fillWidth: true

                    Row {
                        id: crkeDestination

                        anchors.verticalCenter: parent.verticalCenter
                        anchors.left: parent.left
                        anchors.leftMargin: 64*2
                        anchors.margins: 5

                        width: 64
                        spacing: 10
                        opacity: 0.5

                        Repeater {
                            model: vnosModel
                            delegate: DropTile {
                                colorKey: lvl2.barvaPloscic
                                colorText: lvl2.barvaCrk
                                displayLetter: false
                            }
                        }
                    }

                    ListModel {
                        id: vnosModel
                    }

                }
            }

            Rectangle { // Rectangle -> Item
                id: slika
                Layout.preferredHeight: lvl2.height * 2/3
                Layout.preferredWidth: lvl2.width / 3
                color: barva //"red"
                Image {
                    id: slikaImg
                    anchors.verticalCenter: parent.verticalCenter
                    //source: "./images/raca.png"
                    width: parent.width
                    fillMode: Image.PreserveAspectFit
                }
            }
        }

        Rectangle {
            id: crke
            Layout.preferredHeight: lvl2.height/3
            Layout.fillWidth: true
            color: barva  //"yellow"
            anchors.left: parent.left
            anchors.right: parent.right


            Row {
                id: crkeSource

                //anchors.left: parent.left
                anchors.verticalCenter: parent.verticalCenter
                anchors.left: parent.left
                anchors.leftMargin: 64*2

//                anchors.margins: 5
                width: 64
                spacing: 10

                Repeater {
                    model: crkeModel
                    delegate: DragTile { colorKey: lvl2.barvaPloscic; colorText: lvl2.barvaCrk }
                }
            }

            ListModel {
                id: crkeModel
            }

        }
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
                    lvl2.barva = color;
                    var cap = result["fontCapitalization"];
                    lvl2.barvaPloscic = result["barvaPloscic"];
                    lvl2.barvaCrk = result["barvaCrk"];

                    var iskanaBeseda = "avto";
                    readWord(cap, iskanaBeseda);
                } else {
                    console.log("HTTP:", settings.status, settings.statusText);
                }
            }
        }
        settings.send();
    }

    function readWord(capitalization, iskanaBeseda){
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
                        if (list[i].tekst === iskanaBeseda.toLowerCase()){
                            pomocTekst.text = iskanaBeseda;
                            pomocTekst.font.capitalization = capitalization;

                            slikaImg.source = list[i].slika;

                            pomesajCrke(iskanaBeseda);
                            return;
                        }
                    }
                } else {
                    console.log("HTTP:", request.status, request.statusText);
                }
            }
        }
        request.send();
    }

    function pomesajCrke(beseda) {
        var a = beseda.split("");
        var n = a.length;

        for(var c in a){
            vnosModel.append({"crka" : a[c]});
        }

        for(var i = n - 1; i > 0; i--) {
            var j = Math.floor(Math.random() * (i + 1));
            var tmp = a[i];
            a[i] = a[j];
            a[j] = tmp;
        }

        beseda = a.join("");

        for(c in a){
            crkeModel.append({"crka" : a[c]});
        }

    }


}
