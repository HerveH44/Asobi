import React from "react"
import {connect} from 'react-redux';
import {string, array, number, object, func} from "prop-types"
import {Spaced} from "../utils";
import Card from "./Card";
import uniqueId from "lodash.uniqueid"
import {pick, autoPick} from "../../actions/server"

const CardsZone = ({waitingPack, round, pick, autoPick, autoPickId, pickedCards}) => (
    <div>
        <PackZone cards={(waitingPack || {}).cards || []} round={round}
                  pick={pick}
                  autoPick={autoPick}
                  autoPickId={autoPickId}/>
        <MainZone cards={pickedCards}/>
    </div>
);

CardsZone.propTypes = {
    round: number.isRequired,
    pack: array.isRequired,
    pickedCards: array.isRequired,
    waitingPack: object.isRequired,
    autoPick: func.isRequired,
    pick: func.isRequired,
    autoPickId: string.isRequired
};

const PackZone = ({cards, round, pick, autoPick, autoPickId}) => (
    <Grid
        zoneName={"Pack"}
        zoneTitle={`Pack ${round}`}
        // TODO: add pick in state
        zoneSubtitle={`Pick TODO`}
        cards={cards}
        pick={pick}
        autoPick={autoPick}
        autoPickId={autoPickId}/>
);

PackZone.propTypes = {
    cards: array.isRequired,
    round: number.isRequired,
    pick: func.isRequired,
    autoPick: func.isRequired,
    autoPickId: string.isRequired
};

const MainZone = ({cards}) => (
    <Grid
        zoneName={"Main"}
        zoneTitle={"Main"}
        zoneSubtitle={cards.length}
        autoPick={() => ""}
        autoPickId={""}
        pick={() => ""}
        cards={cards}/>
);

MainZone.propTypes = {
    cards: array.isRequired
};

const Grid = ({zoneName, zoneTitle, zoneSubtitle, cards, pick, autoPick, autoPickId}) => (
    <div className='zone' key={uniqueId()}>
        <h1>
            <Spaced elements={[zoneTitle, zoneSubtitle]}/>
        </h1>
        {cards.map(card => <Card key={uniqueId()}
                                 card={card}
                                 zoneName={zoneName}
                                 pick={pick}
                                 autoPick={autoPick}
                                 autoPickId={autoPickId}
        />)}
    </div>
);

Grid.propTypes = {
    zoneName: string.isRequired,
    zoneTitle: string.isRequired,
    zoneSubtitle: string.isRequired,
    cards: array.isRequired,
    pick: func.isRequired,
    autoPick: func.isRequired,
    autoPickId: string.isRequired
};

const mapStateToProps = ({playerState, gameState}) => ({
    ...playerState,
    ...gameState
});
const mapDispatchToProps = {
    pick, autoPick
};

export default connect(mapStateToProps, mapDispatchToProps)(CardsZone);
