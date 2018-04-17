import {createAction, handleActions} from 'redux-actions';

export const editDefault = createAction("EDIT_DEFAULT");

const initialState = {
    siteTitle: "www.dr4ft.info",
    err: "",
    numUsers: 0,
    numPlayers: 0,
    numActiveGames: 0,
    roomsInfo: [],
    motd: ["test motd"],
    repo: "https://github.com/herveh44/asobi",
    version: "FixMe"
};

export default handleActions({
    [editDefault](state, {payload}) {
        console.log("editing default ...", state, payload);
        return {
            ...state,
            ...payload
        };
    }
}, initialState);