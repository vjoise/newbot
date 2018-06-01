import React, {Component} from "react";
import {Button, ButtonGroup, DropdownButton, MenuItem, Panel} from "react-bootstrap";
import * as html2canvas from "html2canvas";


class SidePanel extends Component {
    constructor(props) {
        super(props);

        this.capture = this.capture.bind(this);

        this.state = {
            started: false
        };
    }

    capture() {
        html2canvas(document.getElementById("root"))
            .then(canvas => {
                let screenCapture = document.createElement("a");
                screenCapture.href = canvas.toDataURL("image/png");
                screenCapture.text = "screen capture @ " + new Date().toLocaleTimeString();
                screenCapture.download = screenCapture.text + ".png";

                let screenCaptureDiv = document.createElement("div");
                screenCaptureDiv.id = screenCapture.text;
                screenCaptureDiv.appendChild(screenCapture);
                document.getElementById("panelBody").appendChild(screenCaptureDiv);
            });
    }

    render() {
        return (
            <Panel style={{
                "width": "100%"
            }}>
                {this.state.started ?
                    <Panel.Heading>Please note that all your actions will be logged from now</Panel.Heading>
                    : null}
                {this.state.started ?
                    <Panel.Body id="panelBody">
                        <div id="date">{new Date().toLocaleDateString()}</div>
                    </Panel.Body>
                    : null}
                <Panel.Footer>
                    <ButtonGroup vertical style={{"width": "100%"}}>
                        {this.state.started ?
                            <Button bsStyle="default" id="capture" onClick={this.capture}>Capture Screen</Button>
                            : null}
                        {this.state.started ?
                            <DropdownButton bsStyle="success" title="Complete Monitoring" id="complete-monitoring">
                                <MenuItem eventKey="signOff">Sign Off</MenuItem>
                                <MenuItem eventKey="reject">Reject</MenuItem>
                            </DropdownButton>
                            : null}
                        <Button
                            onClick={() => this.setState({started: !this.state.started})}
                            bsStyle={this.state.started ? "danger" : "success"}>
                            {(!this.state.started ? "Start" : "Stop") + " Monitoring"}</Button></ButtonGroup>
                </Panel.Footer>
            </Panel>);
    }
}

export default SidePanel;