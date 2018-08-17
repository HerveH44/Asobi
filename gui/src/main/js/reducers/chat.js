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
    [GAME_STATE](state, {payload}) {
        const newMessages = state.messages.length !== payload.messages.length;

        return {
            ...state,
            showChat: state.showChat || newMessages,
            messages: payload.messages
        }
    }
}, initialState);
