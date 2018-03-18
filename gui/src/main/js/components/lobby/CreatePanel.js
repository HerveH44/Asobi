import React from "react";
import {connect} from "react-redux";
import {WS_SEND} from "../../actions/websocket";

import _ from "lodash";
// import {Checkbox} from "../utils";

// import GameTypes from "./GameTypes";
// import GameOptions from "./GameOptions";

const CreatePanel = ({title, seats, onChangeTitle, onChangeSeats, onCreateGame}) => {

    return (
        <fieldset className='fieldset'>
            <legend className='legend'>
                Create a room
            </legend>
            {/*<div>*/}
                {/*<label>*/}
                    {/*Game title:{" "}*/}
                    {/*<input type='text'*/}
                           {/*value={title}*/}
                           {/*onChange={onChangeTitle}*/}
                    {/*/>*/}
                {/*</label>*/}
            {/*</div>*/}
            {/*<div>*/}
                {/*Number of players:{" "}*/}
                {/*<select value={seats} onChange={(e) => {*/}
                    {/*App.save("seats", e.currentTarget.value);*/}
                {/*}}>*/}
                    {/*{_.seq(100, 2).map(x =>*/}
                        {/*<option key={_.uid()}>{x}</option>)}*/}
                {/*</select>*/}
            {/*</div>*/}
            {/*<div>*/}
                {/*<Checkbox link='isPrivate' text='Make room private: ' side='right'/>*/}
            {/*</div>*/}
            {/*<GameTypes/>*/}
            {/*<GameOptions/>*/}
            <p>
                <button onClick={onCreateGame}>
                    Create game
                </button>
            </p>
        </fieldset>
    );
};

const mapStateToProps= (state) => ({
  ...state
});

const mapDispatchToProps = (dispatch) => ({
  onCreateGame: () => {
    console.log("sending!");
    dispatch(WS_SEND("test"))
  }
});

export default connect(mapStateToProps, mapDispatchToProps)(CreatePanel);
