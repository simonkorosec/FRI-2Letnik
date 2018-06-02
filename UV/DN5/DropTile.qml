import QtQuick 2.0

DropArea {
    id: dragTarget

    property string colorKey
    property string colorText
    property bool displayLetter
    property alias dropProxy: dragTarget
    property string crka: model.crka
    property bool isCorectPos: false


    width: 75; height: 75
    opacity: 0.5
    keys: [ colorKey ]

    Rectangle {
        id: dropRectangle
        property string crka

        anchors.fill: parent
        color: colorKey

        Text {
            id: tekst
            anchors.fill: parent
            color: colorText
            font.pixelSize: 48
            text: model.crka
            horizontalAlignment:Text.AlignHCenter
            verticalAlignment: Text.AlignVCenter
            font.capitalization: model.capitalization
        }

        states: [
            State {
                when: dragTarget.containsDrag
                PropertyChanges {
                    target: dropRectangle
                    color: "grey"
                }
            },
            State {
                when: isCorectPos === true
                PropertyChanges {
                    target: dragTarget
                    opacity: 1
                }
            },
            State {
                when: isCorectPos === false
                PropertyChanges {
                    target: dragTarget
                    opacity: 0.5
                }
            }
        ]
    }

    Component.onCompleted: function() {
        //crka = tekst.text;
        if(displayLetter === false){
            tekst.text = ""
        }

        parent.arr.append({"drop":this});
    }

}
