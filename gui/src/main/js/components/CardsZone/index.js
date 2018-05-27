import React from "react"
import {connect} from 'react-redux';
import {string, array, number} from "prop-types"
import { Spaced } from "../utils";
import Card from "./Card";
import uniqueId from "lodash.uniqueid"

const CardsZone = ({pack, pickedCards}) => (
    <div>
        <PackZone cards={pack} round={1} pick={1}/>
        <MainZone cards={pickedCards}/>
    </div>
);

CardsZone.propTypes = {
    pack: array.isRequired,
    pickedCards: array.isRequired,
}

const PackZone = ({cards, round, pick}) => (
    <Grid
        zoneName={"Pack"}
        zoneTitle={`Pack ${round}`}
        zoneSubtitle={`Pick ${pick}`}
        cards={cards} />
);

PackZone.propTypes = {
    cards: array.isRequired,
    round: number.isRequired,
    pick: number.isRequired
}

const MainZone = ({cards}) => (
    <Grid
        zoneName={"Main"}
        zoneTitle={"Main"}
        zoneSubtitle={cards.length}
        cards={cards} />
);

MainZone.propTypes = {
    cards: array.isRequired
}

const Grid = ({zoneName, zoneTitle, zoneSubtitle, cards}) => (
    <div className='zone' key={uniqueId()}>
      <h1>
        <Spaced elements={[zoneTitle, zoneSubtitle]}/>
      </h1>
      {cards.map(card => <Card key={uniqueId()} card={card} zoneName={zoneName} />)}
    </div>
);

Grid.propTypes = {
    zoneName: string.isRequired,
    zoneTitle: string.isRequired,
    zoneSubtitle: string.isRequired,
    cards: array.isRequired,
}

const mapStateToProps = ({playerState}) => (
    {
        ...playerState
    }
);
const mapDispatchToProps = {

};

export default connect(mapStateToProps, mapDispatchToProps)(CardsZone);
