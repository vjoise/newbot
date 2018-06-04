import React, {Component} from "react";
import PropTypes from "prop-types";
import {
    Button,
    ButtonGroup,
    DropdownButton,
    Form,
    FormControl,
    FormGroup,
    MenuItem,
    Modal,
    Panel
} from "react-bootstrap";
import * as html2canvas from "html2canvas";
import "../side-panel.css";


class SidePanel extends Component {
    constructor(props) {
        super(props);

        this.start = this.start.bind(this);
        this.eventHandler = this.eventHandler.bind(this);
        this.monitorEvent = this.monitorEvent.bind(this);
        this.stopEventMonitor = this.stopEventMonitor.bind(this);
        this.capture = this.capture.bind(this);
        this.complete = this.complete.bind(this);
        this.sendEvidence = this.sendEvidence.bind(this);
        this.handleShowDialog = this.handleShowDialog.bind(this);
        this.handleCloseDialog = this.handleCloseDialog.bind(this);

        this.state = {
            started: false,
            showDialog: false,
            testPassed: undefined
        };
    }

    render() {
        return (
            <div>
                <Panel defaultExpanded style={{
                    "width": "100%"
                }}>
                    {this.state.started ?
                        <Panel.Heading>
                            <Panel.Title toggle>
                                <div>
                                    <p>Please note that all your actions will be logged from now</p>
                                </div>
                            </Panel.Title>
                        </Panel.Heading>
                        : null}
                    {this.state.started ?
                        <Panel.Collapse>
                            <Panel.Body id="logs">
                                <div id="date">{new Date().toLocaleDateString()}</div>
                            </Panel.Body>
                        </Panel.Collapse>
                        : null}
                    <Panel.Footer>
                        <ButtonGroup vertical style={{"width": "100%"}}>
                            {this.state.started ?
                                <Button bsStyle="default" id="capture" onClick={this.capture}>Capture Screen</Button>
                                : null}
                            {this.state.started ?
                                <DropdownButton bsStyle="success" title="Complete Monitoring" id="complete-monitoring">
                                    <MenuItem eventKey="signOff" onClick={() => this.complete(true)}>Sign Off</MenuItem>
                                    <MenuItem eventKey="reject" onClick={() => this.complete(false)}>Reject</MenuItem>
                                </DropdownButton>
                                : null}
                            <Button
                                onClick={this.start}
                                bsStyle={this.state.started ? "danger" : "success"}>
                                {(!this.state.started ? "Start" : "Stop") + " Monitoring"}</Button>
                        </ButtonGroup>
                    </Panel.Footer>
                </Panel>
                <Modal show={this.state.showDialog} onHide={this.handleCloseDialog}
                       dialogClassName="show-dialog-full-height">
                    <Modal.Dialog style={{"height": "100%"}}>
                        <Modal.Header closeButton>
                            <Modal.Title>
                                <p>Confirm {this.state.testPassed ?
                                    <span style={{"color": "green"}}>sign-off</span> :
                                    <span style={{"color": "red"}}>rejection</span>} for JIRA:</p>
                            </Modal.Title>
                        </Modal.Header>
                        <Modal.Body>
                            <Form>
                                <FormGroup
                                    controlId="jiraKey"
                                >
                                    <FormControl
                                        inputRef={node => this.jiraKey = node}
                                        type="text"
                                        placeholder="Enter JIRA-KEY"
                                        required
                                    />
                                </FormGroup>
                                <FormGroup
                                    controlId="comment"
                                >
                                    <FormControl
                                        inputRef={node => this.comment = node}
                                        componentClass="textarea"
                                        placeholder="If you have any comments..."
                                    />
                                </FormGroup>
                            </Form>
                        </Modal.Body>
                        <Modal.Footer>
                            <Button onClick={this.sendEvidence}>Send Testing Evidence</Button>
                        </Modal.Footer>
                    </Modal.Dialog>
                </Modal>
            </div>);
    }

    start() {
        this.setState({started: !this.state.started});
        if (!this.state.started) {
            this.monitorEvent("click");
            this.monitorEvent("keyup");
        } else {
            this.stopEventMonitor("click");
            this.stopEventMonitor("keyup");
        }
    }

    eventHandler(e) {
        let newDescriptionCell = document.createElement("td");
        newDescriptionCell.style.width = "90%";
        newDescriptionCell.title = e.type + " " + e.target.nodeName + ": " + e.target.outerHTML;
        newDescriptionCell.innerHTML = "<span>" + e.type + " " + e.target.nodeName + ": " + e.target.outerHTML + "</span>";

        let newIcon = document.createElement("span");
        newIcon.classList.add("glyphicon");
        switch (e.type) {
            case "click":
                newIcon.classList.add("glyphicon-hand-up");
                break;
            case "keyup":
                newIcon.classList.add("glyphicon-pencil");
                break;
            default:
        }
        newIcon.style.marginRight = "15px";

        let newIconCell = document.createElement("td");
        newIconCell.style.width = "5px";
        newIconCell.appendChild(newIcon);

        let newRow = document.createElement("tr");
        newRow.appendChild(newIconCell);
        newRow.appendChild(newDescriptionCell);

        let newTableBody = document.createElement("tbody");
        newTableBody.appendChild(newRow);

        let newTable = document.createElement("table");
        newTable.style.marginBottom = 0;
        newTable.style.marginRight = "2px";
        newTable.appendChild(newTableBody);

        let newPanelHeading = document.createElement("div");
        newPanelHeading.classList.add("panel-heading");
        newPanelHeading.appendChild(newTable);

        let newPanel = document.createElement("div");
        newPanel.classList.add("panel");
        newPanel.classList.add("panel-info");
        newPanel.appendChild(newPanelHeading);

        if (document.getElementById("logs")) {
            document.getElementById("logs").appendChild(newPanel);
        }
    }

    monitorEvent(eventType) {
        document.getElementById(this.props.rootElementId).addEventListener(eventType, this.eventHandler);
    }

    stopEventMonitor(eventType) {
        document.getElementById(this.props.rootElementId).removeEventListener(eventType, this.eventHandler);
    }

    capture() {
        html2canvas(document.getElementById(this.props.rootElementId))
            .then(canvas => {
                let screenCapture = document.createElement("a");
                screenCapture.href = canvas.toDataURL("image/png");
                screenCapture.text = "screen capture @ " + new Date().toLocaleTimeString();
                screenCapture.download = screenCapture.text + ".png";

                let screenCaptureDiv = document.createElement("div");
                screenCaptureDiv.id = screenCapture.text;
                screenCaptureDiv.appendChild(screenCapture);
                document.getElementById("logs").appendChild(screenCaptureDiv);
            });
    }

    complete(testPassed) {
        this.setState({testPassed: testPassed});
        this.handleShowDialog();
    }

    sendEvidence() {
        this.jiraKey.value;
        this.comment.value;

        let logs = this.prepareLogs();
        let statusName = "Done";

        // PUT /rest/api/2/issue/{issueIdOrKey}
        // edit status
        // edit description as logs
        // edit title to "Signed-Off" or "Tested and Rejected"

        // POST /rest/api/2/issue/{issueIdOrKey}/comment
        // add comment

        if (this.state.testPassed) {
        } else {
            // PUT /rest/api/2/issue/{issueIdOrKey}/assignee
            // assign to dev
        }

        this.handleCloseDialog();
    }

    handleShowDialog() {
        this.setState({showDialog: true});
    }

    handleCloseDialog() {
        this.setState({showDialog: false});
    }

    prepareLogs() {
        let logItems = document.getElementById("logs").children;
        let logItemsToSend = [];
        for (let i = 0; i < logItems.length; i++) {
            let logItem = logItems[i];
            let logItemToSend;
            if (logItem.classList.contains("panel")) {
                logItemToSend = logItem.children[0].children[0].children[0].children[0].children[1].title;
            } else {
                logItemToSend = logItem.innerHTML;
            }
            logItemsToSend.push(logItemToSend);
        }
        return logItemsToSend;
    }
}

SidePanel.defaultProps = {
    rootElementId: "root"
};

SidePanel.propTypes = {
    rootElementId: PropTypes.string
};

export default SidePanel;