import {handleActions} from 'redux-actions';
import {GAME_STATE, AUTH_TOKEN, onGetDraftLog} from "../actions/server"
import _ from "../lib/utils";

const InitialState = {
    playersStates: [{
        seat: 0,
        isBot: false,
        name: "",
        packs: 0,
        time: 0,
        cockHash: "",
        mwsHash: ""
    }],
    self: 0,
    isHost: true,
    didGameStart: false,
    gameId: "",
    authToken: null,
    title: "",
    seats: 0,
    isPrivate: true,
    gameMode: "",
    gameType: "",
    status: "",
    createdDate: "",
    round: 0,
    packsInfo: "",
    sets: []
};

export default handleActions({
    //TODO: refactor: je veux pas que mes messages arrivent la.
    // Le mieux serait de séparer les infos...
    [GAME_STATE](state, {payload}) {
        return {
            ...state,
            ...payload
        }
    },
    [AUTH_TOKEN](state, {payload}) {
        return {
            ...state,
            ...payload
        }
    },
    [onGetDraftLog](state, {payload: log}) {
        const {gameId, playersStates, self, sets, gameType} = state;
        const isCube = /CUBE/.test(gameType);
        const date = new Date().toISOString().slice(0, -5).replace(/-/g, "").replace(/:/g, "").replace("T", "_");
        const filename = `Draft_${sets.join("_")}_${date}.log`;
        let data = [
            `Event #: ${gameId}`,
            `Time: ${date}`,
            "Players:"
        ];

        playersStates.forEach(({name}, i) =>
            data.push(i === self ? `--> ${name}` : `    ${name}`)
        );

        _.sort(log, "round", "pick");

        log.forEach(({round, pick, cardPicked, cardsPassed}) => {
            if (pick === 1) {
                data.push("", `------ ${isCube ? "Cube" : sets.shift()} ------`);
            }
            data.push("", `Pack ${round} pick ${pick}:`);
            data.push(`--> ${cardPicked}`);
            data = data.concat(cardsPassed.map(x => `    ${x}`));
        });

        _.download(data.join("\n"), filename);

        return state;
    },

}, InitialState);
