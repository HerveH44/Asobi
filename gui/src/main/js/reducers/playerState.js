import {handleActions} from 'redux-actions';
import {PLAYER_STATE, autoPick, leaveGame,} from "../actions/server"
import {onChangeLand, onResetLands} from "../actions/game";

export const MAIN = "Main";
export const SIDE = "Side";
export const JUNK = "Junk";

export const CARDS = {
    Plains: 401994,
    Island: 401927,
    Swamp: 402059,
    Mountain: 401961,
    Forest: 401886
};

const InitialState = {
    waitingPack: {cards: []},
    pickedCards: [],
    [MAIN]: [],
    [SIDE]: [],
    [JUNK]: [],
    autoPickId: ""
};

export default handleActions({
    [PLAYER_STATE](state, {payload}) {
        //Faire diff entre cartes que l'on a déjà et les nouvelles venues du server
        if (state.pickedCards.length < payload.pickedCards.length) {
            Array.prototype.push.apply(
                state[MAIN],
                payload.pickedCards.slice(state.pickedCards.length))
        }

        return {
            ...state,
            ...payload
        }

    },
    [autoPick](state, {payload}) {
        return {
            ...state,
            autoPickId: payload
        }
    },
    [leaveGame]() {
        return InitialState;
    },
    [onChangeLand](state, {payload: {zoneName, cardName, event}}) {
        event.persist();
        let zone = state[zoneName];
        const diff = event.target.value - zone.filter(({name}) => name === cardName).length;

        switch (true) {
            case diff > 0:
                for (let i = 0; i < diff; i++) {
                    zone.push({name: cardName, multiverseid: CARDS[cardName], manaCost: 0})
                }
                break;

            case diff < 0:
                for (let i = diff; i < 0; i++) {
                    const index = zone.map(({name}) => name).indexOf(cardName);
                    zone.splice(index, 1);
                }
                break;
        }

        return {
            ...state
        }
    },
    [onResetLands](state) {
        return {
            ...state,
            [MAIN]: state[MAIN].filter(({name}) => !Object.keys(CARDS).includes(name)),
            [SIDE]: state[SIDE].filter(({name}) => !Object.keys(CARDS).includes(name)),
        };
    }
}, InitialState);
