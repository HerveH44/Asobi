import {handleActions} from 'redux-actions';
import {onToggleChat, onErrorMessage, onGameMessage} from "../actions/game";
import {GAME_STATE} from "../actions/server";

const initialState = {
    showChat: true,
    messages: [],
    errorMessages: []
};

export default handleActions({
    [onToggleChat](state, {payload: event}) {
        event.persist();
        return {
            ...state,
            showChat: event.target.checked,
        }
    },
    [onErrorMessage](state, {payload}) {

        return {
            ...state,
            errorMessages: state.errorMessages.concat([{
                time: new Date(),
                name: "Error",
                text: payload
            }])
        }
    },
    [onGameMessage](state, {payload}) {
        return {
            ...state,
            messages: state.messages.concat([payload])
        }
    },
    [GAME_STATE](state, {payload}) {
        return {
            ...state,
            messages: payload.messages
        }
    }
}, initialState);
