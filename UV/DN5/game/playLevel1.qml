import QtQuick 2.0
import QtQuick.Layouts 1.3

Rectangle {
    id: main
    color: "lightgrey"
    width: 1024
    height: 768

    ColumnLayout {
        anchors.fill: parent

        RowLayout {
            ColumnLayout {
                Rectangle {
                    id: pomoc
                    color: "blue"
                    Layout.preferredHeight: main.height/3
                    Layout.fillWidth: true

                    Text {
                        id: pomocTekst
                        anchors.centerIn: parent
                        text: "napaka"
                        font.pointSize: 24
                        font.capitalization: Font.AllUppercase
                    }
                }
                Rectangle {
                    id: vnos
                    color: "green"
                    Layout.preferredHeight: main.height/3
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
                            delegate: DropTile { colorKey: "red" }
                        }
                    }

                    ListModel {
                        id: vnosModel
                    }

                }
            }

            Rectangle { // Rectangle -> Item
                id: slika
                Layout.preferredHeight: main.height * 2/3
                Layout.preferredWidth: main.width / 3
                color: "red"
                Image {
                    id: slikaImg
                    //source: "./images/raca.png"
                    width: parent.width
                    fillMode: Image.PreserveAspectFit
                }
            }
        }

        Rectangle {
            id: crke
            Layout.preferredHeight: main.height/3
            Layout.fillWidth: true
            color: "yellow"
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
                    delegate: DragTile { colorKey: "red" }
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
                    main.color = color;
                    var cap = result["fontCapitalization"];
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
