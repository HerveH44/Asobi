import React, {Component} from "react"
import {func, object, string} from "prop-types"

class Card extends Component {

    static propTypes = {
        card: object.isRequired,
        onClick: func,
        title: string,
        classNames: string,
        onMouseEnter: func,
        onMouseLeave: func
    };

    constructor(props) {
        super(props);
        const {multiverseid} = props.card;

        this.state = {
            src: this.getUrl(multiverseid)
        }
    }

    getUrl = (multiverseid) => `http://gatherer.wizards.com/Handlers/Image.ashx?multiverseid=${multiverseid}&type=card`;

    onMouseEnter = () => {
        const {doubleFace, flipMultiverseid} = this.props.card;
        if (doubleFace) {
            this.setState({
                src: this.getUrl(flipMultiverseid)
            })
        }
    };

    onMouseLeave = () => {
        const {doubleFace, multiverseid} = this.props.card;
        if(doubleFace) {
            this.setState({
                src: this.getUrl(multiverseid)
            })
        }
    };

    render() {
        const { src } = this.state;
        const { card, onMouseEnter, onMouseLeave, classNames, onClick, title } = this.props;

        return (
            <div className={classNames}
                 onSClick={onClick}
                 title={title}
                 onMouseEnter={onMouseEnter ? onMouseEnter : this.onMouseEnter}
                 onMouseLeave={onMouseLeave ? onMouseLeave : this.onMouseLeave}
                    onDoubleClick={() => console.log("double-click")}>
                <img src={src}
                     alt={card.name}/>
            </div>
        )
    }
}

export default Card;

