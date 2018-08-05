import React from "react";
import {bool, func} from "prop-types";
import {Checkbox} from "../utils";
import SortCards from "./SortCards"
import {connect} from "react-redux";
import {onChangeBeep, onChangeColumnView, onChangePicksToSB, onToggleChat} from "../../actions/game";

const GameSettings = ({onToggleChat, showChat, addPicksToSB, onChangePicksToSB, beep, onChangeBeep, columnView, onChangeColumnView}) => (
    <div className='game-settings'>
        <fieldset className='fieldset'>
            <legend className='legend game-legend'>Settings</legend>
            <span>
                <div>
                    <Checkbox text="Show chat"
                              onChange={onToggleChat}
                              checked={showChat}/>
                </div>
                <div>
                    <Checkbox text="Add picks to sideboard"
                              checked={addPicksToSB}
                              onChange={onChangePicksToSB}/>
                </div>
                <div>
                    <Checkbox text="Beep on new packs"
                              checked={beep}
                              onChange={onChangeBeep}/>
                </div>
                <div>
                    <Checkbox text="Column view"
                              checked={columnView}
                              onChange={onChangeColumnView}/>
                </div>
                <SortCards/>
            </span>
        </fieldset>
    </div>
);

GameSettings.propTypes = {
    onToggleChat: func.isRequired,
    showChat: bool.isRequired,
    addPicksToSB: bool.isRequired,
    onChangePicksToSB: func.isRequired,
    beep: bool.isRequired,
    onChangeBeep: func.isRequired,
    columnView: bool.isRequired,
    onChangeColumnView: func.isRequired
};

const mapStateToProps = ({gameSettings}) => ({
    ...gameSettings,
});

const mapDispatchToProps = {
    onToggleChat,
    onChangePicksToSB,
    onChangeBeep,
    onChangeColumnView
};

export default connect(mapStateToProps, mapDispatchToProps)(GameSettings);

