import React from "react";
import {array, func} from "prop-types";
import Grid from "./Grid";
import {connect} from "react-redux";
import {getCardsAsArray, SIDE} from "../../../reducers/playerState";
import {clickCardZone} from "../../../actions/game";

const SideZone = ({cards, onClickZone}) => (
    <Grid
        zoneName={"Side"}
        zoneTitle={"Side"}
        zoneSubtitle={"" + cards.length}
        addCardClassNames={() => "card"}
        onClickCard={(card) => () => onClickZone({zone: SIDE, card})}
        cards={cards}/>
);

SideZone.propTypes = {
    cards: array.isRequired,
    onClickZone: func.isRequired
};

const mapStateToProps = ({playerState, gameSettings}) => ({
    cards: getCardsAsArray(playerState, SIDE, gameSettings.sort)
});
const mapDispatchToProps = {
    onClickZone: clickCardZone
};

export default connect(mapStateToProps, mapDispatchToProps)(SideZone);
