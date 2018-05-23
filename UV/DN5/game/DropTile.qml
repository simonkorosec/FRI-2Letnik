import QtQuick 2.0

DropArea {
    id: dragTarget

    property string colorKey
    property alias dropProxy: dragTarget
    property string crka: modelData

    width: 64; height: 64
    keys: [ colorKey ]

    Rectangle {
        id: dropRectangle

        anchors.fill: parent
        color: colorKey

        Text {
            anchors.fill: parent
            color: "white"
            font.pixelSize: 48
            text: modelData
            horizontalAlignment:Text.AlignHCenter
            verticalAlignment: Text.AlignVCenter

        }

        states: [
            State {
                when: dragTarget.containsDrag
                PropertyChanges {
                    target: dropRectangle
                    color: "grey"
                }
            }
        ]
    }
}
