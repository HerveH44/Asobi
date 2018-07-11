import React from "react"
import { connect } from 'react-redux';
import { string, array, number, object } from "prop-types"
import { Spaced } from "../utils";
import Card from "./Card";
import uniqueId from "lodash.uniqueid"
import { pick } from "../../actions/server"
import gameState from "../../reducers/gameState";

const CardsZone = ({ waitingPack, round, pick, pickedCards }) => (
    <div>
        <PackZone cards={(waitingPack || {}).cards || []} round={round} pick={pick} />
        <MainZone cards={pickedCards} />
    </div>
);

CardsZone.propTypes = {
    round: number.isRequired,
    pack: array.isRequired,
    pickedCards: array.isRequired,
    waitingPack: object.isRequired
}

const PackZone = ({ cards, round, pick }) => (
    <Grid
        zoneName={"Pack"}
        zoneTitle={`Pack ${round}`}
        // TODO: add pick in state
        zoneSubtitle={`Pick TODO`}
        cards={cards}
        pick={pick} />
);

PackZone.propTypes = {
    cards: array.isRequired,
    round: number.isRequired,
    pick: number.isRequired
}

const MainZone = ({ cards }) => (
    <Grid
        zoneName={"Main"}
        zoneTitle={"Main"}
        zoneSubtitle={cards.length}
        cards={cards} />
);

MainZone.propTypes = {
    cards: array.isRequired
}

const Grid = ({ zoneName, zoneTitle, zoneSubtitle, cards, pick }) => (
    <div className='zone' key={uniqueId()}>
        <h1>
            <Spaced elements={[zoneTitle, zoneSubtitle]} />
        </h1>
        {cards.map(card => <Card key={uniqueId()} card={card} zoneName={zoneName} pick={pick} />)}
    </div>
);

Grid.propTypes = {
    zoneName: string.isRequired,
    zoneTitle: string.isRequired,
    zoneSubtitle: string.isRequired,
    cards: array.isRequired,
}

const mapStateToProps = ({ playerState, gameState }) => ({
    ...playerState,
    ...gameState
});
const mapDispatchToProps = {
    pick
};

export default connect(mapStateToProps, mapDispatchToProps)(CardsZone);
