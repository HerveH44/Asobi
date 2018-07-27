import React, {Component} from "react"
import {string, func, object} from "prop-types"

class Card extends Component {
    constructor(props) {
        super(props);
        this.state = {
            url: `http://gatherer.wizards.com/Handlers/Image.ashx?multiverseid=${props.card.multiverseid}&type=card`
        };
        //   this.onMouseEnter = this.onMouseEnter.bind(this);
        //   this.onMouseLeave = this.onMouseLeave.bind(this);
    }

    onClick(card, autoPickId, pick, autoPick) {
        return () => card.id === autoPickId ? pick(card.id) : autoPick(card.id);
    }

    // onMouseEnter() {
    //   if(this.props.card.isDoubleFaced) {
    //     this.setState({
    //       url: this.props.card.flippedCardURL
    //     });
    //   }
    // }

    // onMouseLeave() {
    //   if(this.props.card.isDoubleFaced) {
    //     this.setState({
    //       url: this.props.card.url
    //     });
    //   }
    // }

    render() {
        const {card, zoneName, pick, autoPick, autoPickId} = this.props;
        const isAutopickable = zoneName === "Pack" && card.id === autoPickId;

        const className =
            `card ${isAutopickable ? "autopick-card " : ""}
        card ${card.foil ? "foil-card " : ""}`;

        const title
            = isAutopickable
            ? "This card will be automatically picked if your time expires."
            : "";
        return (
            <span className={className}
                  title={title}
                  onClick={this.onClick(card, autoPickId, pick, autoPick)}
                // onMouseEnter={this.onMouseEnter}
                // onMouseLeave={this.onMouseLeave}
            >
                <img src={this.state.url} alt={card.name}/>
            </span>
        );
    }
}

Card.propTypes = {
    card: object.isRequired,
    zoneName: string.isRequired,
    pick: func.isRequired,
    autoPick: func.isRequired,
    autoPickId: string.isRequired,
};

export default Card;

